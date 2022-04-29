package com.kt.api.repository;

import com.kt.api.model.entity.MessageQueueEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MqmappingRepository extends CrudRepository<MessageQueueEntity, Long> {

    List<MessageQueueEntity> findByDirection(String direction);
}
