package com.nns.punto_venta.modules.products.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nns.punto_venta.modules.products.entities.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

    // Buscar todos los productos de un usuario específico
    List<ProductEntity> findByUserId(Integer userId);

    // Buscar productos por nombre (que contenga una palabra clave)
    List<ProductEntity> findByNameContainingIgnoreCase(String name);

    // Buscar productos con stock bajo (ej. menor a 5 unidades)
    List<ProductEntity> findByStockLessThan(Integer limit);

    // Buscar todos los productos de un usuario específico paginado.
    Page<ProductEntity> findByUserId(Integer userId, Pageable pageable);
}
