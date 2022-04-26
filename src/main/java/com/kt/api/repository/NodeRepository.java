package com.kt.api.repository;

import com.kt.api.model.entity.NodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface NodeRepository extends JpaRepository<NodeEntity, Long> {
    Optional<NodeEntity> findByNodename(String desc);

    @Query(value= "delete FROM monitoring.node_resource_usage where create_ts < DATE_ADD(NOW(), INTERVAL -30 MINUTES)",
            nativeQuery = true)
    void deleteNodeTableNative();
}
