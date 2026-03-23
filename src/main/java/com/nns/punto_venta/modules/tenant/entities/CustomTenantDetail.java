package com.nns.punto_venta.modules.tenant.entities;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
public class CustomTenantDetail implements UserDetails {


    private final Long id;
    private final String email;
    private final String password;
    private final String schemaName;
    private final String businessCode;
    private final Collection<? extends GrantedAuthority> authorities;


    public CustomTenantDetail(Long id, String email, String password, String schemaName, String businessCode) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.schemaName = schemaName;
        this.businessCode = businessCode;
        this.authorities = List.of(new SimpleGrantedAuthority("Minion"));
    }


    public Long getId() { 
        return id; 
    }
    
    public String getSchemaName() { 
        return schemaName; 
    }
    
    public String getBusinessCode() { 
        return businessCode; 
    }

    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }



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