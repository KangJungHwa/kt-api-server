package com.kt.api.repository;

import com.kt.api.model.entity.GpuEntity;
import com.kt.api.model.entity.GpuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface GpuRepository extends JpaRepository<GpuEntity, Long> {
    Optional<GpuEntity> findByNodename(String desc);


    @Query(value= "delete FROM monitoring.gpu_status where create_ts < DATE_ADD(NOW(), INTERVAL -30 MINUTES)",
            nativeQuery = true)
    void deleteGpuTableNative();
}
