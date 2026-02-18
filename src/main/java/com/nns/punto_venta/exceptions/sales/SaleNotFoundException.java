package com.nns.punto_venta.exceptions.sales;

import com.nns.punto_venta.exceptions.ResourceNotFoundException;

public class SaleNotFoundException extends ResourceNotFoundException {
    public SaleNotFoundException(String message) {
        super(message);
    }
    
}
