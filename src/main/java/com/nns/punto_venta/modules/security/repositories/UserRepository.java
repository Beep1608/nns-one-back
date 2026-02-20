package com.nns.punto_venta.modules.security.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nns.punto_venta.modules.security.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {


    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    Page<UserEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
