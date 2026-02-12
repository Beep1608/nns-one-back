package com.nns.punto_venta.mappers.users;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.nns.punto_venta.dtos.users.UserRequestDto;
import com.nns.punto_venta.dtos.users.UserResponseDto;
import com.nns.punto_venta.entities.PermissionEntity;

import com.nns.punto_venta.entities.RoleEntity;
import com.nns.punto_venta.entities.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoles")
    @Mapping(target = "permissions", source = "roles", qualifiedByName = "mapPermissions")
    UserResponseDto toResponse(UserEntity entity);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true) 
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "sales", ignore = true)
    UserEntity toEntity(UserRequestDto dto);
    
    @Named("mapRoles")
    default Set<String> mapRoles(Set<RoleEntity> roles) {
        if (roles == null) return java.util.Collections.emptySet();
        return roles.stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());
    }

    @Named("mapPermissions")
    default Set<String> mapPermissions(Set<RoleEntity> roles) {
        if (roles == null) return java.util.Collections.emptySet();
        return roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(PermissionEntity::getName)
                .collect(Collectors.toSet());
    }
}