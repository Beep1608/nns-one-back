package com.nns.punto_venta.modules.security.assemblers;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.nns.punto_venta.modules.security.controllers.RoleController;
import com.nns.punto_venta.modules.security.dtos.RoleResponseDto;
import com.nns.punto_venta.modules.security.entities.RoleEntity;
import com.nns.punto_venta.modules.security.mappers.RoleMapper;

@Component
public class RoleModelAssembler implements RepresentationModelAssembler<RoleEntity, RoleResponseDto> {
    
    private RoleMapper roleMapper;
    public RoleModelAssembler(RoleMapper roleMapper){
        this.roleMapper = roleMapper;

    }

    @Override
    public RoleResponseDto toModel(RoleEntity entity) {

        RoleResponseDto model = roleMapper.toResponse(entity);

        model.add(linkTo(methodOn(RoleController.class).getById(entity.getId())).withSelfRel());

        return model;
    }
    
}

