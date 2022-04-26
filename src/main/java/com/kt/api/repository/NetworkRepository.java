package com.kt.api.repository;

import com.kt.api.model.entity.NetworkEntity;
import com.kt.api.model.entity.NodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface NetworkRepository extends JpaRepository<NetworkEntity, Long> {
    Optional<NetworkEntity> findByNodename(String desc);

    @Query(value= "delete FROM monitoring.network_status where create_ts < DATE_ADD(NOW(), INTERVAL -30 MINUTES)",
            nativeQuery = true)
    void deleteNetworkTableNative();
}
