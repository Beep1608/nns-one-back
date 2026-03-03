package com.nns.punto_venta.modules.tenant.services;

import javax.sql.DataSource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.nns.punto_venta.modules.tenant.dtos.TenantRequestDto;
import com.nns.punto_venta.modules.tenant.dtos.TenantResponseDto;
import com.nns.punto_venta.modules.tenant.mappers.TenantMapper;
import com.nns.punto_venta.modules.tenant.repositories.TenantRepository;
import com.nns.punto_venta.modules.tenant.resolvers.TenantIdentifierResolver;

@Service
public class TenantService {
    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;
    private final TenantIdentifierResolver tenantIdentifierResolver;
    private final DataSource dataSource;

    public TenantService(TenantRepository tenantRepository, TenantMapper tenantMapper, TenantIdentifierResolver tenantIdentifierResolver, DataSource dataSource) {
        this.tenantRepository = tenantRepository;
        this.tenantMapper = tenantMapper;
        this.tenantIdentifierResolver = tenantIdentifierResolver;
        this.dataSource = dataSource;
    }
    public TenantResponseDto createTenant(TenantRequestDto tenantDto) {

        tenantIdentifierResolver.setCurrentTenant("tenants");
        
        try {
          
            return saveTenant(tenantDto);
        }catch (Exception e) {
            throw new RuntimeException("Error creating tenant: " + e.getMessage(), e);
        }
         finally {
            tenantIdentifierResolver.clear();
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TenantResponseDto saveTenant(TenantRequestDto tenantDto){
      
        var entity = tenantMapper.toEntity(tenantDto);
        String schemaName = StringUtils.trimAllWhitespace(entity.getName().toLowerCase());
        entity.setSchemaName(schemaName);
        var savedEntity = tenantRepository.save(entity);
        tenantRepository.executeCreateTenantFunction(savedEntity.getSchemaName());
        return tenantMapper.toResponseDto(savedEntity);
    }

}
