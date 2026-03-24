package com.nns.punto_venta.modules.tenant.context;

public class TenantContext {
    private static final ThreadLocal<Long> CURRENT_TENANT = new ThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_SCHEMA = new ThreadLocal<>();

    public static void setCurrentTenant(Long id){
        CURRENT_TENANT.set(id);
    }

    public static Long getCurrentTenant (){
        return CURRENT_TENANT.get();
    }

    public static void setCurrentSchema(String schema){
        CURRENT_SCHEMA.set(schema);
    }

    public static String getCurrentSchema(){
        return CURRENT_SCHEMA.get();
    }

    public static void clear() {
        CURRENT_TENANT.remove();
        CURRENT_SCHEMA.remove();
    }
}
