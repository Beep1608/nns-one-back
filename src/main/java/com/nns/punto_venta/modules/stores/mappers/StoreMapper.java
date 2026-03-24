package com.nns.punto_venta.modules.stores.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nns.punto_venta.modules.stores.dtos.StoreRequestDto;
import com.nns.punto_venta.modules.stores.dtos.StoreResponseDto;
import com.nns.punto_venta.modules.stores.entities.StoreEntity;

@Mapper(componentModel = "spring")
public interface StoreMapper {

    @Mapping(source = "region.id", target = "regionId")
    @Mapping(source = "region.name", target = "regionName")
    StoreResponseDto toResponseDto(StoreEntity entity);

    List<StoreResponseDto> toResponseDtoList(List<StoreEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "region", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    StoreEntity toEntity(StoreRequestDto dto);
}
