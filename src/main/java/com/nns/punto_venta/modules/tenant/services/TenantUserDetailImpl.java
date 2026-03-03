package com.nns.punto_venta.modules.tenant.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nns.punto_venta.modules.tenant.entities.TenantEntity;
import com.nns.punto_venta.modules.tenant.exceptions.TenantNotFoundException;
import com.nns.punto_venta.modules.tenant.repositories.TenantRepository;

@Service
public class TenantUserDetailImpl implements UserDetailsService {
    private final TenantRepository tenantRepository;
    
    public TenantUserDetailImpl (TenantRepository tenantRepository){
        this.tenantRepository = tenantRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TenantEntity entity = tenantRepository.findByNameOrEmail(username)
        .orElseThrow(() -> new TenantNotFoundException("No pudimos encontrar al administrador."));
        
        
        return new User(
            entity.getEmail(),
            entity.getPassword(),
            null
        );
    }
    
}
