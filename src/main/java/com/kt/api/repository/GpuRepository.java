package com.kt.api.repository;

import com.kt.api.model.entity.GpuEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface GpuRepository extends CrudRepository<GpuEntity, Long> {
    Optional<GpuEntity> findByNodename(String desc);

    @Transactional
    @Query(value= "delete FROM monitoring.gpu_status where create_ts < DATE_ADD(NOW(), INTERVAL -30 MINUTE)",
            nativeQuery = true)
    void deleteGpuTableNative();
}
