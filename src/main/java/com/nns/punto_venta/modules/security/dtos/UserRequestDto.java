package com.nns.punto_venta.modules.security.dtos;

import java.util.List;

import com.nns.punto_venta.modules.security.validators.RolesExist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserRequestDto {

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;

    @NotBlank(message = "El rol no puede estar vacío")
    @RolesExist // Validación personalizada para la base de datos y catálogo
    private String role;

    private List<Long> storeIds;

    public UserRequestDto(){}
    public UserRequestDto(String username, String password){this.username=username; this.password=password;}
    public UserRequestDto(String username){this.username=username;}

    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getRole() {return role;}

    public void setRole(String role) {this.role = role;}

    public List<Long> getStoreIds() { return storeIds; }

    public void setStoreIds(List<Long> storeIds) { this.storeIds = storeIds; }
}

