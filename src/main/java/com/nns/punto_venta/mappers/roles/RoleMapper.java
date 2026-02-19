package com.nns.punto_venta.mappers.roles;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nns.punto_venta.dtos.roles.RoleRequestDto;
import com.nns.punto_venta.dtos.roles.RoleResponseDto;
import com.nns.punto_venta.entities.RoleEntity;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    // Entidad -> DTO (Para la respuesta)
    //@Mapping(target = "permissions", source = "permissions", qualifiedByName = "mapPermissionNames")
    RoleResponseDto toResponse(RoleEntity entity);

    // DTO -> Entidad (Para crear/actualizar)
    @Mapping(target = "id", ignore = true)
   // @Mapping(target = "permissions", ignore = true) // Ignoramos para validar en el servicio
    RoleEntity toEntity(RoleRequestDto dto);

    // Convierte Set<PermissionEntity> -> Set<String> (Solo nombres)
    //@Named("mapPermissionNames")
    //default Set<String> mapPermissionNames(Set<PermissionEntity> permissions) {
    //    if (permissions == null) return java.util.Collections.emptySet();
    //    return permissions.stream()
    //            .map(PermissionEntity::getName)
    //            .collect(Collectors.toSet());
    //}
}