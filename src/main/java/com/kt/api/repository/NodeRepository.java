package com.kt.api.repository;

import com.kt.api.model.entity.NodeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface NodeRepository extends CrudRepository<NodeEntity, Long> {
    Optional<NodeEntity> findByNodename(String desc);

    @Transactional
    @Query(value= "delete FROM monitoring.node_resource_usage where create_ts < DATE_ADD(NOW(), INTERVAL -30 MINUTE)",
            nativeQuery = true)
    void deleteNodeTableNative();
}
