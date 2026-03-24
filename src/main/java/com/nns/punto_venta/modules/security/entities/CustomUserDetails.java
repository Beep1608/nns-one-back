package com.nns.punto_venta.modules.security.entities;

import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    
    private final Long id;
    private final String email;
    private final String password;
    private final String schemaName;
    private final String businessCode;
    private final Collection<? extends GrantedAuthority> authorities;

    // constructor para inicializar los campos
    public CustomUserDetails(Long id, String email, String password, String schemaName, 
                             String businessCode, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.schemaName = schemaName;
        this.businessCode = businessCode;
        this.authorities = authorities;
    }

    // getters para tus campos personalizados
    public Long getId() {
        return id;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    // métodos sobreescritos de userdetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    @Nullable
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        // retornamos el email ya que suele ser el identificador principal para el login
        return email;
    }

    // spring security requiere implementar estos 4 métodos de estado de la cuenta.
    // por defecto los dejamos en true, pero puedes conectarlos a tu base de datos en el futuro.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}