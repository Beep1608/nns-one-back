package com.nns.punto_venta.dtos.users;

import jakarta.validation.constraints.Size;

public class UserUpdateRequestDto {
    

    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    private String username;

    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;

    public UserUpdateRequestDto(){}
    public UserUpdateRequestDto(String username, String password){this.username=username; this.password=password;}

    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
}
