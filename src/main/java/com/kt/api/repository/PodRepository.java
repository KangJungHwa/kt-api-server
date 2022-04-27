package com.kt.api.repository;
import com.kt.api.model.entity.PodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PodRepository extends JpaRepository<PodEntity, Long> {
    Optional<PodEntity> findByPodname(String desc);

    @Query(value= "delete FROM monitoring.pod_resource_usage where create_ts < DATE_ADD(NOW(), INTERVAL -30 MINUTE)",
            nativeQuery = true)
    void deletePodTableNative();
}
