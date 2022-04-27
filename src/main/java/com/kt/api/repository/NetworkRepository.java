package com.kt.api.repository;

import com.kt.api.model.entity.NetworkEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface NetworkRepository extends CrudRepository<NetworkEntity, Long> {
    Optional<NetworkEntity> findByNodename(String desc);

    @Transactional
    @Modifying
    @Query(value= "delete from monitoring.network_status where create_ts < date_add(now(), interval -30 minute)",
            nativeQuery = true)
    void deleteNetworkTableNative();
}
