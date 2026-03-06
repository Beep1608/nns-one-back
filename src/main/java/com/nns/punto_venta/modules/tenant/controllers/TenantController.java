package com.nns.punto_venta.modules.tenant.controllers;

import javax.sql.DataSource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nns.punto_venta.modules.tenant.dtos.TenantLoginRequestDto;
import com.nns.punto_venta.modules.tenant.dtos.TenantLoginResponseDto;
import com.nns.punto_venta.modules.tenant.dtos.TenantRequestDto;
import com.nns.punto_venta.modules.tenant.dtos.TenantResponseDto;
import com.nns.punto_venta.modules.tenant.services.TenantService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;



@RestController
@RequestMapping("/api/tenants")
public class TenantController {
    private final TenantService tenantService;
    private final DataSource dataSource;
    public TenantController(TenantService tenantService, DataSource dataSource) {
        this.tenantService = tenantService;
        this.dataSource = dataSource;
    }   


    @PostMapping("/register")
    public ResponseEntity<TenantResponseDto> createTenant(@RequestBody TenantRequestDto tenantDto) {
          
        var createdTenant = tenantService.createTenant(tenantDto);

        return ResponseEntity.ok(createdTenant);
    }

    @PostMapping("/login")
    public ResponseEntity<TenantLoginResponseDto> login(TenantLoginRequestDto loginRequestDto){

        return  ResponseEntity.ok(tenantService.login(loginRequestDto));
    }


}
