package com.nns.punto_venta.exceptions.products;

import com.nns.punto_venta.exceptions.ResourceNotFoundException;

public class ProductNotFoundException extends ResourceNotFoundException {
    public ProductNotFoundException(String message) {
        super(message);
    }
    
}
