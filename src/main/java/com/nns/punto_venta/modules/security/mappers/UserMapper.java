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
import com.nns.punto_venta.modules.stores.entities.StoreEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoles")
    @Mapping(target = "storeIds", source = "stores", qualifiedByName = "mapStoreIds")
    @Mapping(target = "storeNames", source = "stores", qualifiedByName = "mapStoreNames")
    UserResponseDto toResponse(UserEntity entity);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true) 
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "sales", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "stores", ignore = true)
    UserEntity toEntity(UserRequestDto dto);
    
    @Named("mapRoles")
    default Set<String> mapRoles(Set<RoleEntity> roles) {
        if (roles == null) return java.util.Collections.emptySet();
        return roles.stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());
    }

    @Named("mapStoreIds")
    default List<Long> mapStoreIds(Set<StoreEntity> stores) {
        if (stores == null) return java.util.Collections.emptyList();
        return stores.stream()
                .map(StoreEntity::getId)
                .toList();
    }

    @Named("mapStoreNames")
    default Set<String> mapStoreNames(Set<StoreEntity> stores) {
        if (stores == null) return java.util.Collections.emptySet();
        return stores.stream()
                .map(StoreEntity::getName)
                .collect(Collectors.toSet());
    }
}
