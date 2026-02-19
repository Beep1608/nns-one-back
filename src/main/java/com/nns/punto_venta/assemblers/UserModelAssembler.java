package com.nns.punto_venta.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.nns.punto_venta.controllers.UserController;
import com.nns.punto_venta.dtos.users.UserResponseDto;
import com.nns.punto_venta.entities.UserEntity;
import com.nns.punto_venta.mappers.users.UserMapper;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserEntity, UserResponseDto> {

    private  UserMapper userMapper;

    public UserModelAssembler(UserMapper userMapper) {
        
        this.userMapper = userMapper;
    }

    @Override
    public UserResponseDto toModel(UserEntity entity) {
        // 1. Convertimos la entidad a DTO usando el mapper
        UserResponseDto model = userMapper.toResponse(entity);

        // 2. Añadimos el enlace "self" (detalle del usuario) automáticamente
        // Esto genera: /api/users/{id}
        model.add(linkTo(methodOn(UserController.class).getById(entity.getId())).withSelfRel());

        return model;
    }
}