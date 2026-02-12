package com.nns.punto_venta.dtos.users;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;

public class UserResponseDto {
    private Integer id;
    private String username;
    private String createdAt;
    private String lastActiveAt;

    private Set<String> roles;
    private Set<String> permissions;


    public UserResponseDto(){}
    public UserResponseDto(Integer id, String username){this.id=id; this.username=username;}
    public UserResponseDto(Integer id, String username, String createdAt, String lastActiveAt, Set<String> roles, Set<String> permissions) {
        this.id = id;
        this.username = username;
        this.createdAt = createdAt;
        this.lastActiveAt = lastActiveAt;
        this.roles = roles;
        this.permissions = permissions;
    }
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = formatDate(createdAt);
    }

    public String getLastActiveAt() { return lastActiveAt; }
    public void setLastActiveAt(OffsetDateTime lastActiveAt) {
        this.lastActiveAt = formatDate(lastActiveAt);
    }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }

    public Set<String> getPermissions() { return permissions; }
    public void setPermissions(Set<String> permissions) { this.permissions = permissions; }

    private String formatDate(OffsetDateTime date) {
        if (date == null) return null;
        Locale locale = Locale.of("es", "MX");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy HH:mm", locale);
        return date.format(formatter);
    }
    
}