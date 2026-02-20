package com.nns.punto_venta.modules.security.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nns.punto_venta.modules.security.dtos.RoleResponseDto;
import com.nns.punto_venta.modules.security.entities.RoleEntity;
import com.nns.punto_venta.modules.security.exceptions.UserNotFoundException;
import com.nns.punto_venta.modules.security.mappers.RoleMapper;
import com.nns.punto_venta.modules.security.repositories.RoleRepository;


@Service
public class RoleService {
    private RoleRepository roleRepository;
    private RoleMapper roleMapper;
    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper){
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }
    
    @Transactional(readOnly = true)
    public Page<RoleEntity> findAll(Pageable pageable) {

        return roleRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public RoleResponseDto findById(Long id) {
                return roleRepository.findById(id)
                .map(roleMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("Rol no encontrado con ID: " + id));
    }
}

