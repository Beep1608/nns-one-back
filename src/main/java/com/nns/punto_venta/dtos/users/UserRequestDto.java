package com.nns.punto_venta.dtos.users;

public class UserRequestDto {

    private String username;
    private String password;

    public UserRequestDto(){}
    public UserRequestDto(String username, String password){this.username=username; this.password=password;}
    public UserRequestDto(String username){this.username=username;}

    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
}
