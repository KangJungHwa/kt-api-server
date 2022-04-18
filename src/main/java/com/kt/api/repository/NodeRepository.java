package com.kt.api.repository;

import com.kt.api.model.entity.NodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface NodeRepository extends JpaRepository<NodeEntity, Long> {
    Optional<NodeEntity> findByNodename(String desc);
}
