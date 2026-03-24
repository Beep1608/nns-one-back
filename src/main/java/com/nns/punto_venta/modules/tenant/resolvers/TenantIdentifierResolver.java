package com.nns.punto_venta.modules.tenant.resolvers;

import java.util.Map;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import com.nns.punto_venta.modules.tenant.context.TenantContext;

@Component
public class TenantIdentifierResolver 
implements CurrentTenantIdentifierResolver<String>, HibernatePropertiesCustomizer {

    public void setCurrentTenant(String currentTenant){
        TenantContext.setCurrentSchema(currentTenant);
    }

    @Override
    public String resolveCurrentTenantIdentifier() {
       String schema = TenantContext.getCurrentSchema();
       return schema != null ? schema : "public";
    }

    @Override
    public boolean validateExistingCurrentSessions() {
       return false;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }
    public void clear() {
        TenantContext.clear();
    }
    
}
