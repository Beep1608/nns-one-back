package com.nns.punto_venta.configuration.security.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nns.punto_venta.entities.UserEntity;
import com.nns.punto_venta.repositories.UserRepository;


@Service
public class UserDetailsImpl implements UserDetailsService{

    private final UserRepository userRepository;
    public UserDetailsImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Buscamos el usuario en la base de datos
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con username: " + username));

        // 2. Extraemos roles y permisos y los convertimos en GrantedAuthority
        List<SimpleGrantedAuthority> authorities = userEntity.getRoles().stream()
                .flatMap(role -> {
                    // Convertimos el nombre del Rol (ROLE_ADMIN)
                    Stream<SimpleGrantedAuthority> roleAuth = Stream.of(new SimpleGrantedAuthority(role.getName()));
                    
                    // Convertimos los nombres de los Permisos (user:read)
                    Stream<SimpleGrantedAuthority> permissionAuth = role.getPermissions().stream()
                            .map(p -> new SimpleGrantedAuthority(p.getName()));
                    
                    // Combinamos ambos en un solo flujo
                    return Stream.concat(roleAuth, permissionAuth);
                })
                .collect(Collectors.toList());

        // 3. Retornamos el UserDetails que espera el framework
        return new org.springframework.security.core.userdetails.User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                authorities
        );
    }
}
