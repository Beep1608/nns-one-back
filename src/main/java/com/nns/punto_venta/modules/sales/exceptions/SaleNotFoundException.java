package com.nns.punto_venta.modules.sales.exceptions;

import com.nns.punto_venta.common.exceptions.ResourceNotFoundException;

public class SaleNotFoundException extends ResourceNotFoundException {
    public SaleNotFoundException(String message) {
        super(message);
    }
    
}

