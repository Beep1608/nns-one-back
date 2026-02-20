package com.nns.punto_venta.modules.security.services;

import java.util.HashSet;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nns.punto_venta.modules.security.dtos.UserResponseDto;
import com.nns.punto_venta.modules.security.dtos.UserRequestDto;
import com.nns.punto_venta.modules.security.dtos.UserUpdateRequestDto;
import com.nns.punto_venta.modules.security.entities.RoleEntity;
import com.nns.punto_venta.modules.security.entities.UserEntity;
import com.nns.punto_venta.modules.security.exceptions.UserNotFoundException;
import com.nns.punto_venta.modules.security.mappers.UserMapper;
import com.nns.punto_venta.modules.security.repositories.RoleRepository;
import com.nns.punto_venta.modules.security.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // Obtener todos los usuarios
    @Transactional(readOnly = true)
    public Page<UserEntity> findAll(Pageable pageable) {

        return userRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    // Buscar un usuario por ID
    @Transactional(readOnly = true)
    public UserResponseDto findById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("Usuarios no encontrado con ID: " + id));
    }

    // Crear un usuario (Recibimos RequestDto y devolvemos ResponseDto)
    @Transactional
    public UserResponseDto createUser(UserRequestDto dto) {

  // 1. Convertimos los campos básicos
    UserEntity userEntity = userMapper.toEntity(dto);

     // 2. VALIDACIÓN: Buscamos los roles reales en la DB
     List<RoleEntity> rolesFound = roleRepository.findAllById(dto.getRoles());

     // Si el tamaño no coincide, es que algún ID no existía
     if (rolesFound.size() != dto.getRoles().size()) {
         throw new EntityNotFoundException("Uno o más roles no fueron encontrados");
     }

     // 3. Asignamos los roles reales (que ya traen su Name y Permissions)
     userEntity.setRoles(new HashSet<>(rolesFound));

     // 4. Password y Guardado
     userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
     UserEntity savedUser = userRepository.save(userEntity);

     // 5. Ahora el Mapper sí encontrará los nombres y permisos
     return userMapper.toResponse(savedUser);
    }

    @Transactional
    public UserResponseDto updateUser(Integer id, UserUpdateRequestDto dto) {
        // 1. Buscamos el usuario existente
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + id));

        // 2. Actualizamos los campos básicos
        user.setUsername(dto.getUsername());

        // 3. Lógica de contraseña: Solo actualizamos y encriptamos si el DTO trae una
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // 4. Guardamos y mapeamos a respuesta
        UserEntity updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    // Eliminar un usuario
    @Transactional
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }

    // Búsqueda personalizada
    @Transactional(readOnly = true)
    public UserResponseDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Username no encontrado: " + username));
    }
}

