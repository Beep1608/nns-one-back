package com.nns.punto_venta.modules.tenant.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nns.punto_venta.modules.tenant.entities.TenantEntity;

@Repository
public interface  TenantRepository extends JpaRepository<TenantEntity, Long> {
    
    Optional<TenantEntity> findByNameOrEmail (String text);
    
    @Query(value="SELECT create_tenant(:schemaName)", nativeQuery = true)
    void executeCreateTenantFunction(@Param("schemaName") String schemaName);
}
