package com.nns.punto_venta.modules.sales.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nns.punto_venta.modules.sales.dtos.SaleRequestDto;
import com.nns.punto_venta.modules.sales.dtos.SaleResponseDto;
import com.nns.punto_venta.modules.sales.entities.SaleEntity;

@Mapper(componentModel = "spring")
public interface SaleMapper {
    @Mapping(source = "user.username", target = "username")
    SaleResponseDto toResponseDto(SaleEntity entity);


    // ENTRADA: De DTO a Entidad (para guardar en la base de datos)
    // Ignoramos los campos que Hibernate genera solos o que seteamos a mano
    @Mapping(target = "id", ignore = true) // El ID lo genera la DB
    @Mapping(target = "user", ignore = true) // Se busca por ID en el Service
    @Mapping(target = "status", ignore = true) // Tiene valor por defecto "PENDIENTE"
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    SaleEntity toEntity(SaleRequestDto dto);
}


