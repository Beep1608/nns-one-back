package com.nns.punto_venta.modules.tenant.services;

import java.security.SecureRandom;
import java.text.Normalizer;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.nns.punto_venta.modules.security.services.JwtService;
import com.nns.punto_venta.modules.tenant.dtos.TenantLoginRequestDto;
import com.nns.punto_venta.modules.tenant.dtos.TenantLoginResponseDto;
import com.nns.punto_venta.modules.tenant.dtos.TenantRequestDto;
import com.nns.punto_venta.modules.tenant.dtos.TenantResponseDto;
import com.nns.punto_venta.modules.tenant.entities.CustomTenantDetail;
import com.nns.punto_venta.modules.tenant.mappers.TenantMapper;
import com.nns.punto_venta.modules.tenant.repositories.TenantRepository;
import com.nns.punto_venta.modules.tenant.resolvers.TenantIdentifierResolver;

@Service
public class TenantService {
    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;
    private final TenantIdentifierResolver tenantIdentifierResolver;
    private final DataSource dataSource;
    private final JwtService jwtService;
    private final TenantUserDetailImpl tenantUserDetailImpl;
    private final BCryptPasswordEncoder encoder;


    @Value("${tenant.code.characters}")
    private String characters;

    @Value("${tenant.code.suffix-length}")
    private int suffixLength;

    private final SecureRandom secureRandom = new SecureRandom();

    private final AuthenticationManager authenticationManager;

    public TenantService(TenantRepository tenantRepository, 
        JwtService jwtService,
        TenantUserDetailImpl tenantUserDetailImpl,
        TenantMapper tenantMapper, 
        TenantIdentifierResolver tenantIdentifierResolver, 
        AuthenticationManager authenticationManager,
        BCryptPasswordEncoder encoder, 
        DataSource dataSource) {
        this.tenantRepository = tenantRepository;
        this.jwtService = jwtService;
        this.tenantUserDetailImpl = tenantUserDetailImpl;
        this.tenantMapper = tenantMapper;
        this.tenantIdentifierResolver = tenantIdentifierResolver;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.dataSource = dataSource;
    }
    public ResponseEntity<TenantResponseDto> createTenant(TenantRequestDto tenantDto) {

        tenantIdentifierResolver.setCurrentTenant("tenants");
        
        try {
          
            return ResponseEntity.ok(saveTenant(tenantDto));
        }catch (Exception e) {
            throw new RuntimeException("Error creating tenant: " + e.getMessage(), e);
        }
         finally {
            tenantIdentifierResolver.clear();
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TenantResponseDto saveTenant(TenantRequestDto tenantDto){
      
        if (tenantRepository.existsByEmail(tenantDto.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }
        
        if (tenantRepository.existsByName(tenantDto.getName())) {
            throw new IllegalArgumentException("El nombre de la organización ya está en uso. Por favor, elige otro.");
        }
        var entity = tenantMapper.toEntity(tenantDto);
        String schemaName = StringUtils.trimAllWhitespace(entity.getName().toLowerCase());
        entity.setSchemaName(schemaName);
        entity.setBusinessCode(generateBusinessCode(schemaName));
        entity.setPassword(encoder.encode(entity.getPassword()));
        var savedEntity = tenantRepository.save(entity);
        tenantRepository.executeCreateTenantFunction(savedEntity.getSchemaName());
        return tenantMapper.toResponseDto(savedEntity);
    }

    public ResponseEntity<TenantLoginResponseDto> login (TenantLoginRequestDto loginRequestDto){
       Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequestDto.getName(),
                loginRequestDto.getPassword()
            )
       );

       CustomTenantDetail userDetails  = (CustomTenantDetail) auth.getPrincipal();
       String token = jwtService.generateTokenTenant(userDetails);

       System.out.println("Token : "+ token);
       TenantLoginResponseDto loginResponseDto = new TenantLoginResponseDto();
       loginResponseDto.setToken(token);
        return  ResponseEntity.ok(loginResponseDto);
    }


    public String generateBusinessCode(String companyName) {
        
        String cleanName = cleanString(companyName);
        
        
        if (cleanName.isEmpty()) {
            cleanName = "ORG"; 
        }

        
        String prefix = cleanName.length() >= 4 ? cleanName.substring(0, 4) : cleanName;

        
        StringBuilder suffix = new StringBuilder(suffixLength);
        for (int i = 0; i < suffixLength; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            suffix.append(characters.charAt(randomIndex));
        }

        
        return prefix + "-" + suffix.toString();
    }

    /**
     * Elimina acentos, espacios y caracteres especiales.
     */
    private String cleanString(String input) {
        if (input == null) return "";
        
        
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        
       
        return normalized.replaceAll("\\p{M}", "") 
                         .replaceAll("[^a-zA-Z0-9]", "") 
                         .toUpperCase();
    }
}

