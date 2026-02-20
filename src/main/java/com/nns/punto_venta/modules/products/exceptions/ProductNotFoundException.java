package com.nns.punto_venta.modules.products.exceptions;

import com.nns.punto_venta.common.exceptions.ResourceNotFoundException;

public class ProductNotFoundException extends ResourceNotFoundException {
    public ProductNotFoundException(String message) {
        super(message);
    }
    
}

