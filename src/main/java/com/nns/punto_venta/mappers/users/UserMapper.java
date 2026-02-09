package com.nns.punto_venta.mappers.users;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nns.punto_venta.dtos.users.UserRequestDto;
import com.nns.punto_venta.dtos.users.UserResponseDto;
import com.nns.punto_venta.entities.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserResponseDto toResponse(UserEntity entity);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true) 
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "sales", ignore = true)
    UserEntity toEntity(UserRequestDto dto);
}