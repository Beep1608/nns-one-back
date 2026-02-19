package com.nns.punto_venta.assemblers;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.nns.punto_venta.controllers.RoleController;
import com.nns.punto_venta.dtos.roles.RoleResponseDto;
import com.nns.punto_venta.entities.RoleEntity;
import com.nns.punto_venta.mappers.roles.RoleMapper;

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
