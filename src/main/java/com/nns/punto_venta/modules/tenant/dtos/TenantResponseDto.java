package com.nns.punto_venta.modules.tenant.dtos;

public class TenantResponseDto {

    private String name;
    private String businessCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }   

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }
    
}
