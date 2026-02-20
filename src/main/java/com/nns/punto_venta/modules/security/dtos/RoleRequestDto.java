package com.nns.punto_venta.modules.security.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RoleRequestDto {
    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String name;

    public RoleRequestDto() {}
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

