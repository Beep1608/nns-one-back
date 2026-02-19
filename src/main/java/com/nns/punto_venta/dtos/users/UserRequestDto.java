package com.nns.punto_venta.dtos.users;

import java.util.List;

import com.nns.punto_venta.validators.interfaces.RolesExist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class UserRequestDto {

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;

    @NotEmpty(message = "La lista de roles no puede estar vacía")
    @RolesExist // Validación personalizada para la base de datos
    private List<@NotNull(message = "El ID del rol no puede ser nulo") 
             @Positive(message = "El ID del rol debe ser un número positivo") 
             Long> roles;

    public UserRequestDto(){}
    public UserRequestDto(String username, String password){this.username=username; this.password=password;}
    public UserRequestDto(String username){this.username=username;}

    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public List<Long> getRoles() {return roles;}

    public void setRoles(List<Long> roles) {this.roles = roles;}
}
