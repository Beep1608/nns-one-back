package com.nns.punto_venta.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nns.punto_venta.entities.SaleEntity;

@Repository
public interface SaleRepository extends JpaRepository<SaleEntity, Integer> {

    // Obtener el historial de ventas de un usuario específico
    List<SaleEntity> findByUserId(Integer userId);

    // Buscar ventas por estado (ej. "PENDIENTE", "PAGADO")
    List<SaleEntity> findByStatus(String status);

    // Buscar ventas de un usuario con un estado específico
    List<SaleEntity> findByUserIdAndStatus(Integer userId, String status);

    // Buscar ventas cuyo monto total sea mayor a cierta cantidad
    List<SaleEntity> findByTotalAmountGreaterThan(java.math.BigDecimal amount);
    
    // Obtener el historial de ventas de un usuario específico paginado
    Page<SaleEntity> findByUserId(Integer userId, Pageable pageable);
}