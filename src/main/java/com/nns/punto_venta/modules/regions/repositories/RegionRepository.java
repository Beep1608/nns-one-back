package com.nns.punto_venta.modules.regions.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nns.punto_venta.modules.regions.entities.RegionEntity;

@Repository
public interface RegionRepository extends JpaRepository<RegionEntity, Long> {
}
