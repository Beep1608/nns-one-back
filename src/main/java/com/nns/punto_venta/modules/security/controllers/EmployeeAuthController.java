package com.nns.punto_venta.modules.security.controllers;

import com.nns.punto_venta.modules.security.dtos.AuthResponseDTO;
import com.nns.punto_venta.modules.security.dtos.EmployeeLoginRequestDTO;
import com.nns.punto_venta.modules.security.services.EmployeeAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/employee")
@RequiredArgsConstructor
public class EmployeeAuthController {

    private final EmployeeAuthService employeeAuthService;

    /**
     * Endpoint for employee login.
     * REMINDER: This endpoint MUST be configured as public (permitAll()) in SecurityConfig.java
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody EmployeeLoginRequestDTO request) {
        return ResponseEntity.ok(employeeAuthService.authenticateEmployee(request));
    }
}
