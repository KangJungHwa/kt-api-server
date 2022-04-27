package com.kt.api.repository;

import com.kt.api.model.entity.GpuEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface GpuRepository extends CrudRepository<GpuEntity, Long> {
    Optional<GpuEntity> findByNodename(String desc);

    @Transactional
    @Modifying
    @Query(value= "delete from monitoring.gpu_status where create_ts < date_add(now(), interval -30 minute)", nativeQuery = true)
    void deleteGpuTableNative();
}

