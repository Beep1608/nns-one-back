package com.nns.punto_venta.modules.security.mappers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.nns.punto_venta.modules.security.dtos.UserRequestDto;
import com.nns.punto_venta.modules.security.dtos.UserResponseDto;
import com.nns.punto_venta.modules.security.entities.PermissionEntity;

import com.nns.punto_venta.modules.security.entities.RoleEntity;
import com.nns.punto_venta.modules.security.entities.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoles")
    //@Mapping(target = "permissions", source = "roles", qualifiedByName = "mapPermissions")
    UserResponseDto toResponse(UserEntity entity);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true) 
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "sales", ignore = true)
    @Mapping(target = "roles", ignore = true)
    UserEntity toEntity(UserRequestDto dto);
    
    // 1. Convierte List<Long> -> Set<RoleEntity>
    @Named("idsToEntities")
    default Set<RoleEntity> idsToEntities(List<Long> roleIds) {
        if (roleIds == null) return new HashSet<>();
        return roleIds.stream()
                .map(id -> {
                    RoleEntity role = new RoleEntity();
                    role.setId(id);
                    return role;
                })
                .collect(Collectors.toSet());
    }
    
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
