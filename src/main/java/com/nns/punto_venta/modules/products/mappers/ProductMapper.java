package com.nns.punto_venta.modules.products.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nns.punto_venta.modules.products.dtos.ProductRequestDto;
import com.nns.punto_venta.modules.products.dtos.ProductResponseDto;
import com.nns.punto_venta.modules.products.entities.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // 1. Entity -> ResponseDto (Para mostrar datos)
    // Extraemos el username de la relación UserEntity
    @Mapping(source = "user.username", target = "sellerName")
    ProductResponseDto toResponseDto(ProductEntity entity);

    // Mapeo de listas para el GET all
    List<ProductResponseDto> toResponseDtoList(List<ProductEntity> entities);

    // 2. RequestDto -> Entity (Para guardar/crear)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // Lo seteamos en el Service con el userId
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    ProductEntity toEntity(ProductRequestDto dto);
}
