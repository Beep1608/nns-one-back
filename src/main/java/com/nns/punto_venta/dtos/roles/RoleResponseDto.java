package com.nns.punto_venta.dtos.roles;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Relation(collectionRelation = "roles", itemRelation = "rol")
@JsonPropertyOrder({ "id", "name", "_links" })
public class RoleResponseDto extends RepresentationModel<RoleResponseDto> {
    
    private Integer id;
    private String name;

    public RoleResponseDto() {}

    public RoleResponseDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = StringUtils.capitalize(name);
    }


}
