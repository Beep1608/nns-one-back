package com.nns.punto_venta.modules.regions.exceptions;

import com.nns.punto_venta.common.exceptions.ResourceNotFoundException;

public class RegionNotFoundException extends ResourceNotFoundException {
    public RegionNotFoundException(String message) {
        super(message);
    }
}
