package com.nns.punto_venta.modules.security.controllers;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nns.punto_venta.modules.security.assemblers.UserModelAssembler;
import com.nns.punto_venta.modules.security.dtos.UserRequestDto;
import com.nns.punto_venta.modules.security.dtos.UserResponseDto;
import com.nns.punto_venta.modules.security.dtos.UserUpdateRequestDto;
import com.nns.punto_venta.modules.security.entities.UserEntity;
import com.nns.punto_venta.modules.security.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name="Usuarios", description="Operaciones relacionadas con los usuarios")
public class UserController {

    private UserService userService;
    private UserModelAssembler userAssembler;

    public UserController(UserService userService, UserModelAssembler userAssembler)
    {
        this.userService = userService;
        this.userAssembler = userAssembler;
    }

    // Obtener todos los usuarios: GET http://localhost:8080/api/users
    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios registrados en el sistema.")
    public ResponseEntity<PagedModel<UserResponseDto>> getAll(
        @ParameterObject Pageable pageable,
        PagedResourcesAssembler<UserEntity> pagedAssembler
    ) {
        Page<UserEntity> users = userService.findAll(pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(users,this.userAssembler));
    }
    
    // Obtener uno por ID: GET http://localhost:8080/api/users/1
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario por ID", description = "Devuelve un usuario específico según su ID.")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente",content=@Content(mediaType="application/json", schema=@Schema(implementation=UserResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado con ID:{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Integer id) {
        
        return ResponseEntity.ok(userService.findById(id));
    }

    // Crear un usuario: POST http://localhost:8080/api/users
    @PostMapping
    @Operation(summary = "Crear un nuevo usuario", description = "Crea un nuevo usuario en el sistema.")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",content=@Content(mediaType="application/json", schema=@Schema(implementation=UserResponseDto.class)))
    public ResponseEntity<UserResponseDto> create(@Valid @ParameterObject UserRequestDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }

    // Actualizar un usuario: PUT http://localhost:8080/api/users/1
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario existente", description = "Actualiza los datos de un usuario existente en el sistema.")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",content=@Content(mediaType="application/json", schema=@Schema(implementation=UserUpdateRequestDto.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado con ID:{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable Integer id, @Valid @ParameterObject UserUpdateRequestDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    // Eliminar un usuario: DELETE http://localhost:8080/api/users/1
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario (soft delete)", description = "Elimina un usuario específico del sistema según su ID.")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado con ID:{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
