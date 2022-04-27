package com.kt.api.repository;
import com.kt.api.model.entity.PodEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PodRepository extends CrudRepository<PodEntity, Long> {
    Optional<PodEntity> findByPodname(String desc);


    @Transactional
    @Modifying
    @Query(value= "delete FROM monitoring.pod_resource_usage where create_ts < date_add(now(), interval -30 minute)",
            nativeQuery = true)
    void deletePodTableNative();
}
