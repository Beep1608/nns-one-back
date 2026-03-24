package com.nns.punto_venta.modules.stores.exceptions;

import com.nns.punto_venta.common.exceptions.ResourceNotFoundException;

public class StoreNotFoundException extends ResourceNotFoundException {
    public StoreNotFoundException(String message) {
        super(message);
    }
}
