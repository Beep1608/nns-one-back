package com.nns.punto_venta.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nns.punto_venta.dtos.users.UserRequestDto;
import com.nns.punto_venta.dtos.users.UserResponseDto;
import com.nns.punto_venta.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    // Obtener todos los usuarios: GET http://localhost:8080/api/users
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }
    
    // Obtener uno por ID: GET http://localhost:8080/api/users/1
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Integer id) {
        // El service ya lanza excepción o maneja el DTO, así que es directo
        return ResponseEntity.ok(userService.findById(id));
    }

    // Crear un usuario: POST http://localhost:8080/api/users
    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userDto));
    }

    // Actualizar un usuario: PUT http://localhost:8080/api/users/1
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable Integer id, @RequestBody UserRequestDto userDto) {
        return ResponseEntity.ok(userService.update(id, userDto));
    }

    // Eliminar un usuario: DELETE http://localhost:8080/api/users/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}