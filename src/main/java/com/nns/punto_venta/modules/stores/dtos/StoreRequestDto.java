package com.nns.punto_venta.modules.stores.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StoreRequestDto {

    private Long regionId;

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    @Size(max = 255)
    private String name;

    private String address;

    @Size(max = 50)
    private String status = "active";

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
