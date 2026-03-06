package com.nns.punto_venta.modules.tenant.services;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
        System.out.println("Intentando Autenticar Tenant");
        TenantEntity entity = tenantRepository.findByNameOrEmail(username)
        .orElseThrow(() -> new TenantNotFoundException("No pudimos encontrar al administrador."));
        System.out.println(entity.getEmail());
        System.out.println(entity.getPassword());
        System.out.println(entity.getBusinessCode());
        return new User(
            entity.getEmail(),
            entity.getPassword(),
            List.of(new SimpleGrantedAuthority("minion"))
        );
    }
    
}
