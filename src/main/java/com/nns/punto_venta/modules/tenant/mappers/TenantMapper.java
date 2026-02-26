package com.nns.punto_venta.modules.tenant.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nns.punto_venta.modules.tenant.dtos.TenantRequestDto;
import com.nns.punto_venta.modules.tenant.dtos.TenantResponseDto;
import com.nns.punto_venta.modules.tenant.entities.TenantEntity;

@Mapper(componentModel = "spring")
public interface  TenantMapper {
    @Mapping(source = "name", target = "name")
    TenantResponseDto toResponseDto(TenantEntity entity);


    // ENTRADA: De DTO a Entidad (para guardar en la base de datos)
    // Ignoramos los campos que Hibernate genera solos o que seteamos a mano
    @Mapping(target = "id", ignore = true) // El ID lo genera la DB
    @Mapping(target = "schemaName", ignore = true) // Tiene valor por defecto "PENDIENTE"
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TenantEntity toEntity(TenantRequestDto dto);

}
