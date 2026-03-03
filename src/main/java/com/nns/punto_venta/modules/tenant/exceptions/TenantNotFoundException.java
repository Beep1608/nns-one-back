package com.nns.punto_venta.modules.tenant.exceptions;

public class TenantNotFoundException extends RuntimeException {
    public TenantNotFoundException(String message) {
        super(message);
    }
    
}
