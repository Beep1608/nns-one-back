package com.nns.punto_venta.modules.stores.services;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nns.punto_venta.modules.regions.entities.RegionEntity;
import com.nns.punto_venta.modules.regions.repositories.RegionRepository;
import com.nns.punto_venta.modules.stores.dtos.StoreRequestDto;
import com.nns.punto_venta.modules.stores.dtos.StoreResponseDto;
import com.nns.punto_venta.modules.stores.entities.StoreEntity;
import com.nns.punto_venta.modules.stores.exceptions.StoreNotFoundException;
import com.nns.punto_venta.modules.stores.mappers.StoreMapper;
import com.nns.punto_venta.modules.stores.repositories.StoreRepository;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final RegionRepository regionRepository;
    private final StoreMapper storeMapper;

    public StoreService(StoreRepository storeRepository, RegionRepository regionRepository, StoreMapper storeMapper) {
        this.storeRepository = storeRepository;
        this.regionRepository = regionRepository;
        this.storeMapper = storeMapper;
    }

    @Transactional(readOnly = true)
    public List<StoreResponseDto> findAll() {
        List<StoreEntity> entities = storeRepository.findAll();
        return storeMapper.toResponseDtoList(entities);
    }

    @Transactional(readOnly = true)
    public StoreResponseDto findById(Long id) {
        StoreEntity entity = storeRepository.findById(id)
                .orElseThrow(() -> new StoreNotFoundException("Sucursal no encontrada con ID: " + id));
        return storeMapper.toResponseDto(entity);
    }

    @Transactional
    public StoreResponseDto create(StoreRequestDto dto) {
        validateOwnerRole();
        StoreEntity entity = storeMapper.toEntity(dto);
        
        if (dto.getRegionId() != null) {
            RegionEntity region = regionRepository.findById(dto.getRegionId())
                    .orElseThrow(() -> new RuntimeException("Región no encontrada"));
            entity.setRegion(region);
        }

        StoreEntity savedEntity = storeRepository.save(entity);
        return storeMapper.toResponseDto(savedEntity);
    }

    @Transactional
    public StoreResponseDto update(Long id, StoreRequestDto dto) {
        validateOwnerRole();
        StoreEntity entity = storeRepository.findById(id)
                .orElseThrow(() -> new StoreNotFoundException("Sucursal no encontrada con ID: " + id));
        
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setStatus(dto.getStatus());

        if (dto.getRegionId() != null) {
            RegionEntity region = regionRepository.findById(dto.getRegionId())
                    .orElseThrow(() -> new RuntimeException("Región no encontrada"));
            entity.setRegion(region);
        } else {
            entity.setRegion(null);
        }
        
        StoreEntity updatedEntity = storeRepository.save(entity);
        return storeMapper.toResponseDto(updatedEntity);
    }

    @Transactional
    public void delete(Long id) {
        validateOwnerRole();
        if (!storeRepository.existsById(id)) {
            throw new StoreNotFoundException("Sucursal no encontrada con ID: " + id);
        }
        storeRepository.deleteById(id);
    }

    private void validateOwnerRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new InsufficientAuthenticationException("Usuario no autenticado");
        }

        boolean isOwner = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equalsIgnoreCase("OWNER") 
                            || a.getAuthority().equalsIgnoreCase("ADMIN")
                            || a.getAuthority().equalsIgnoreCase("ROLE_ADMIN")
                            || a.getAuthority().equalsIgnoreCase("Minion"));

        if (!isOwner) {
            throw new AccessDeniedException("Acceso denegado: Se requiere rol de OWNER para gestionar sucursales.");
        }
    }
}
