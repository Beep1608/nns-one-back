package com.nns.punto_venta.modules.security.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeLoginRequestDTO {
    private String businessCode;
    private String username;
    private String password;
}
