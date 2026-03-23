package com.nns.punto_venta.modules.tenant.dtos;

public class TenantLoginResponseDto {

    private String token; 

    public void setToken(String token){
        this.token=token;
    }
    public String getToken(){
        return token;
    }
    
}
