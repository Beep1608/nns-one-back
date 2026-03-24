package com.nns.punto_venta.modules.regions.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegionRequestDto {

    @NotBlank(message = "El nombre de la región es obligatorio")
    @Size(max = 255)
    private String name;

    @Size(max = 50)
    private String timezone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
