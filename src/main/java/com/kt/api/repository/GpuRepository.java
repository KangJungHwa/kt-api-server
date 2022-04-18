package com.kt.api.repository;

import com.kt.api.model.entity.GpuEntity;
import com.kt.api.model.entity.NodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface GpuRepository extends JpaRepository<GpuEntity, Long> {
    Optional<GpuEntity> findByNodename(String desc);
}
