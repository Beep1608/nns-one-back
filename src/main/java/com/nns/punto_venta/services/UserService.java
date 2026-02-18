package com.nns.punto_venta.services;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nns.punto_venta.dtos.users.UserRequestDto;
import com.nns.punto_venta.dtos.users.UserResponseDto;
import com.nns.punto_venta.dtos.users.UserUpdateRequestDto;
import com.nns.punto_venta.entities.UserEntity;
import com.nns.punto_venta.exceptions.users.UserNotFoundException;
import com.nns.punto_venta.mappers.users.UserMapper;
import com.nns.punto_venta.repositories.UserRepository;

@Service
public class UserService {
     
    private UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, BCryptPasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // Obtener todos los usuarios
    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        List<UserEntity> users = userRepository.findAllByOrderByCreatedAtDesc();
        return users.stream()
                    .map(userMapper::toResponse)
                    .toList();
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
        // 1. Convertimos DTO a Entidad
        UserEntity userEntity = userMapper.toEntity(dto);
        
        // 2. Lógica de negocio (Ej: Encriptar password antes de guardar)
        // userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        userEntity.setPassword(encodedPassword);
        
        // 3. Guardamos y convertimos el resultado a respuesta
        UserEntity savedUser = userRepository.save(userEntity);
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
