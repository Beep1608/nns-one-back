package com.nns.punto_venta.exceptions.users;

import com.nns.punto_venta.exceptions.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
    
}
