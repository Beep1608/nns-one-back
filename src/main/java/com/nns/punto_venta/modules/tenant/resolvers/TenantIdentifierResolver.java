package com.nns.punto_venta.modules.tenant.resolvers;

import java.util.Map;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

@Component
public class TenantIdentifierResolver 
implements CurrentTenantIdentifierResolver<String>, HibernatePropertiesCustomizer {

   private static final ThreadLocal<String> currentTenant = ThreadLocal.withInitial(() -> "public");

    public void setCurrentTenant(String currentTenant){
        TenantIdentifierResolver.currentTenant.set(currentTenant);
    }

    @Override
    public String resolveCurrentTenantIdentifier() {
       return currentTenant.get();
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
        currentTenant.remove();
    }
    
}
