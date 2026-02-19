package com.nns.punto_venta.controllers;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nns.punto_venta.assemblers.RoleModelAssembler;
import com.nns.punto_venta.dtos.roles.RoleResponseDto;
import com.nns.punto_venta.entities.RoleEntity;
import com.nns.punto_venta.services.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/roles")
@Tag(name="Roles", description="Operaciones relacionadas con los roles")
public class RoleController {
    
    private RoleService roleService;
    private RoleModelAssembler roleAssembler;
    public RoleController(RoleService roleService, RoleModelAssembler roleAssembler){
        this.roleService = roleService;
        this.roleAssembler = roleAssembler;
    }
        
    
    @GetMapping
    @Operation(summary = "Obtener todos los roles", description = "Devuelve una lista de todos los roles registrados en el sistema.")
    public ResponseEntity<PagedModel<RoleResponseDto>> getAll(
        @ParameterObject Pageable pageable,
        PagedResourcesAssembler<RoleEntity> pagedAssembler
    ) {
        Page<RoleEntity> roles = roleService.findAll(pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(roles,this.roleAssembler));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un rol por ID", description = "Devuelve un rol específico según su ID.")
    @ApiResponse(responseCode = "200", description = "Rol encontrado exitosamente",content=@Content(mediaType="application/json", schema=@Schema(implementation=RoleResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Rol no encontrado con ID:{id}")
    public ResponseEntity<RoleResponseDto> getById(@PathVariable Long id) {
        
        return ResponseEntity.ok(roleService.findById(id));
    }
}
