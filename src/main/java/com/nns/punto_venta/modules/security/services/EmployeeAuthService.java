package com.nns.punto_venta.modules.security.services;

import com.nns.punto_venta.modules.security.dtos.AuthResponseDTO;
import com.nns.punto_venta.modules.security.dtos.EmployeeLoginRequestDTO;
import com.nns.punto_venta.modules.security.entities.CustomUserDetails;
import com.nns.punto_venta.modules.security.entities.UserEntity;
import com.nns.punto_venta.modules.security.repositories.UserRepository;
import com.nns.punto_venta.modules.tenant.context.TenantContext;
import com.nns.punto_venta.modules.tenant.entities.TenantEntity;
import com.nns.punto_venta.modules.tenant.repositories.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EmployeeAuthService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public AuthResponseDTO authenticateEmployee(EmployeeLoginRequestDTO request) {
        // Step 1: Query the master/public schema to find the Tenant by businessCode
        // Note: TenantRepository should be configured to always query the 'tenants' or 'public' schema
        // based on the @Table(schema="tenants") annotation in TenantEntity.
        TenantEntity tenant = tenantRepository.findByBusinessCode(request.getBusinessCode())
                .orElseThrow(() -> new BadCredentialsException("Invalid business code, username, or password"));

        // Step 2: Extract the schemaName associated with that businessCode
        String schemaName = tenant.getSchemaName();

        try {
            // Step 3: Switch the database context (search_path) to that schemaName
            TenantContext.setCurrentSchema(schemaName);

            // Step 4: Find the employee in the users table of that specific schema using the username
            UserEntity user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("Invalid business code, username, or password"));

            // Step 5: Verify the password using Spring's PasswordEncoder
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("Invalid business code, username, or password");
            }

            // Step 6: Generate a JWT token
            CustomUserDetails userDetails = buildCustomUserDetails(user, schemaName, request.getBusinessCode());
            String token = jwtService.generateTokenEmployee(userDetails);

            return AuthResponseDTO.builder()
                    .token(token)
                    .build();
        } finally {
            // Always clear the context to avoid leaks
            TenantContext.clear();
        }
    }

    private CustomUserDetails buildCustomUserDetails(UserEntity user, String schemaName, String businessCode) {
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .flatMap(role -> {
                    Stream<SimpleGrantedAuthority> roleAuth = Stream.of(new SimpleGrantedAuthority(role.getName()));
                    Stream<SimpleGrantedAuthority> permissionAuth = role.getPermissions().stream()
                            .map(p -> new SimpleGrantedAuthority(p.getName()));
                    return Stream.concat(roleAuth, permissionAuth);
                })
                .collect(Collectors.toList());

        return new CustomUserDetails(
                user.getId().longValue(),
                user.getUsername(),
                user.getPassword(),
                schemaName,
                businessCode,
                authorities
        );
    }
}
