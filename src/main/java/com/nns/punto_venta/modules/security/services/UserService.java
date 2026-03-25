package com.nns.punto_venta.modules.security.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nns.punto_venta.modules.security.dtos.UserRequestDto;
import com.nns.punto_venta.modules.security.dtos.UserResponseDto;
import com.nns.punto_venta.modules.security.dtos.UserUpdateRequestDto;
import com.nns.punto_venta.modules.security.entities.RoleEntity;
import com.nns.punto_venta.modules.security.entities.UserEntity;
import com.nns.punto_venta.modules.security.exceptions.UserNotFoundException;
import com.nns.punto_venta.modules.security.mappers.UserMapper;
import com.nns.punto_venta.modules.security.repositories.RoleRepository;
import com.nns.punto_venta.modules.security.repositories.UserRepository;
import com.nns.punto_venta.modules.stores.entities.StoreEntity;
import com.nns.punto_venta.modules.stores.exceptions.StoreNotFoundException;
import com.nns.punto_venta.modules.stores.repositories.StoreRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StoreRepository storeRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, StoreRepository storeRepository, UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.storeRepository = storeRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // Obtener todos los usuarios
    @Transactional(readOnly = true)
    public Page<UserEntity> findAll(Pageable pageable) {
        validateOwnerRole();
        return userRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    // Buscar un usuario por ID
    @Transactional(readOnly = true)
    public UserResponseDto findById(Integer id) {
        validateOwnerRole();
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + id));
    }

    // Crear un usuario
    @Transactional
    public UserResponseDto createUser(UserRequestDto dto) {
        validateOwnerRole();

        // 1. Convertimos los campos básicos
        UserEntity userEntity = userMapper.toEntity(dto);

        // 2. Asignar Rol (Único: Admin o User)
        String roleName = dto.getRole().toLowerCase();
        if (!roleName.equals("admin") && !roleName.equals("user")) {
            throw new IllegalArgumentException("El rol debe ser 'admin' o 'user'");
        }

        RoleEntity roleFound = roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("El rol '" + roleName + "' no fue encontrado"));
        userEntity.setRoles(new HashSet<>(List.of(roleFound)));

        // 3. Asignar Sucursales
        assignStores(userEntity, roleName, dto.getStoreIds());

        // 4. Password y Guardado
        userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        UserEntity savedUser = userRepository.save(userEntity);

        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public UserResponseDto updateUser(Integer id, UserUpdateRequestDto dto) {
        validateOwnerRole();
        
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + id));

        user.setUsername(dto.getUsername());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // Si se implementa actualización de rol/sucursales en el DTO de update:
        // String roleName = user.getRoles().iterator().next().getName().toLowerCase();
        // assignStores(user, roleName, dto.getStoreIds());

        UserEntity updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    // Eliminar un usuario (Soft Delete)
    @Transactional
    public void deleteUser(Integer id) {
        validateOwnerRole();
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + id));
        
        user.setDeletedAt(java.time.OffsetDateTime.now());
        userRepository.save(user);
    }

    private void assignStores(UserEntity user, String roleName, List<Long> storeIds) {
        if (storeIds == null || storeIds.isEmpty()) {
            if (roleName.equals("user")) {
                throw new IllegalArgumentException("Un usuario con rol 'user' debe estar asignado a exactamente una sucursal.");
            }
            user.setStores(new HashSet<>());
            return;
        }

        // Eliminar duplicados y validar unicidad
        Set<Long> uniqueIds = new HashSet<>(storeIds);
        if (uniqueIds.size() != storeIds.size()) {
            throw new IllegalArgumentException("La lista de sucursales contiene IDs duplicados.");
        }

        if (roleName.equals("user") && uniqueIds.size() != 1) {
            throw new IllegalArgumentException("Un usuario con rol 'user' solo puede estar asignado a una sucursal.");
        }

        // Buscar todas las sucursales de una sola vez
        List<StoreEntity> foundStores = storeRepository.findAllById(uniqueIds);
        
        if (foundStores.size() != uniqueIds.size()) {
            Set<Long> foundIds = foundStores.stream().map(StoreEntity::getId).collect(Collectors.toSet());
            List<Long> missingIds = uniqueIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toList());
            
            throw new StoreNotFoundException("No se encontraron las siguientes sucursales: " + missingIds);
        }
        
        user.setStores(new HashSet<>(foundStores));
    }

    private void validateOwnerRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new InsufficientAuthenticationException("Usuario no autenticado");
        }

        boolean isOwner = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equalsIgnoreCase("OWNER") 
                            || a.getAuthority().equalsIgnoreCase("Minion"));

        if (!isOwner) {
            throw new AccessDeniedException("Acceso denegado: Se requiere rol de OWNER para gestionar empleados.");
        }
    }

}