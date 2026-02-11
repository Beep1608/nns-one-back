package com.nns.punto_venta.dtos.users;

public class UserResponseDto {
    private Integer id;
    private String username;

    public UserResponseDto(){}
    public UserResponseDto(Integer id, String username){ this.id = id; this.username = username;}
    // Getters y Setters
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
}