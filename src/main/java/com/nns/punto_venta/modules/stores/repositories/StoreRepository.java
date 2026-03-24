package com.nns.punto_venta.modules.stores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nns.punto_venta.modules.stores.entities.StoreEntity;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
}
