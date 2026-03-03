package com.nns.punto_venta.modules.tenant.context;

public class TenantContext {
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static void setCurrentTenant(String id){
        CURRENT_TENANT.set(id);
    }

    public static String getCurrentTenant (){
        return CURRENT_TENANT.get();
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
