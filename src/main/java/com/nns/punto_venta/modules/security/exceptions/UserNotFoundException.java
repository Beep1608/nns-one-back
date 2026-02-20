package com.nns.punto_venta.modules.security.exceptions;

import com.nns.punto_venta.common.exceptions.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
    
}

