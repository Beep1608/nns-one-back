package com.nns.punto_venta.modules.regions.services;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nns.punto_venta.modules.regions.dtos.RegionRequestDto;
import com.nns.punto_venta.modules.regions.dtos.RegionResponseDto;
import com.nns.punto_venta.modules.regions.entities.RegionEntity;
import com.nns.punto_venta.modules.regions.exceptions.RegionNotFoundException;
import com.nns.punto_venta.modules.regions.mappers.RegionMapper;
import com.nns.punto_venta.modules.regions.repositories.RegionRepository;

@Service
public class RegionService {

    private final RegionRepository regionRepository;
    private final RegionMapper regionMapper;

    public RegionService(RegionRepository regionRepository, RegionMapper regionMapper) {
        this.regionRepository = regionRepository;
        this.regionMapper = regionMapper;
    }

    @Transactional(readOnly = true)
    public List<RegionResponseDto> findAll() {
        List<RegionEntity> entities = regionRepository.findAll();
        return regionMapper.toResponseDtoList(entities);
    }

    @Transactional(readOnly = true)
    public RegionResponseDto findById(Long id) {
        RegionEntity entity = regionRepository.findById(id)
                .orElseThrow(() -> new RegionNotFoundException("Región no encontrada con ID: " + id));
        return regionMapper.toResponseDto(entity);
    }

    @Transactional
    public RegionResponseDto create(RegionRequestDto dto) {
        validateOwnerRole();
        RegionEntity entity = regionMapper.toEntity(dto);
        RegionEntity savedEntity = regionRepository.save(entity);
        return regionMapper.toResponseDto(savedEntity);
    }

    @Transactional
    public RegionResponseDto update(Long id, RegionRequestDto dto) {
        validateOwnerRole();
        RegionEntity entity = regionRepository.findById(id)
                .orElseThrow(() -> new RegionNotFoundException("Región no encontrada con ID: " + id));
        
        entity.setName(dto.getName());
        if (dto.getTimezone() != null) {
            entity.setTimezone(dto.getTimezone());
        }
        
        RegionEntity updatedEntity = regionRepository.save(entity);
        return regionMapper.toResponseDto(updatedEntity);
    }

    @Transactional
    public void delete(Long id) {
        validateOwnerRole();
        if (!regionRepository.existsById(id)) {
            throw new RegionNotFoundException("Región no encontrada con ID: " + id);
        }
        regionRepository.deleteById(id);
    }

    /**
     * Valida que el usuario autenticado tenga el rol de 'OWNER' o 'ADMIN'.
     * Según las reglas de negocio, solo los dueños/admin del tenant pueden gestionar regiones.
     */
    private void validateOwnerRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new InsufficientAuthenticationException("Usuario no autenticado");
        }

        boolean isOwner = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equalsIgnoreCase("OWNER") 
                            || a.getAuthority().equalsIgnoreCase("ADMIN")
                            || a.getAuthority().equalsIgnoreCase("ROLE_ADMIN")
                            || a.getAuthority().equalsIgnoreCase("Minion")); // 'Minion' es el valor actual en CustomTenantDetail

        if (!isOwner) {
            throw new AccessDeniedException("Acceso denegado: Se requiere rol de OWNER para gestionar regiones.");
        }
    }
}
