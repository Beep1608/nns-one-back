package com.nns.punto_venta.modules.regions.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nns.punto_venta.modules.regions.dtos.RegionRequestDto;
import com.nns.punto_venta.modules.regions.dtos.RegionResponseDto;
import com.nns.punto_venta.modules.regions.entities.RegionEntity;

@Mapper(componentModel = "spring")
public interface RegionMapper {

    RegionResponseDto toResponseDto(RegionEntity entity);

    List<RegionResponseDto> toResponseDtoList(List<RegionEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RegionEntity toEntity(RegionRequestDto dto);
}
