package com.nns.punto_venta.modules.tenant.dtos;

public class TenantRequestDto {
    
    private String name;
    private String schemaName = name;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }
    
}
