package com.nns.punto_venta.modules.security.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nns.punto_venta.modules.security.entities.RoleEntity;

@Repository
public interface  RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);

    long countByIdIn(List<Long> ids);
    
}

