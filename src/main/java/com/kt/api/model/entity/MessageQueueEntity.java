package com.kt.api.model.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "nlu_mq_mapping")
public class MessageQueueEntity{

    @Id
    private Integer id;

    @Column(name = "route_key", columnDefinition = "VARCHAR(40)", nullable = true)
    private String routeKey;

    @Column(name = "direction", columnDefinition = "VARCHAR(20)", nullable = true)
    private String direction;

    @Column(name = "queue_name", columnDefinition = "VARCHAR(40)", nullable = true)
    private String queueName;

    @Column(name = "exchange", columnDefinition = "VARCHAR(40)", nullable = true)
    private String exchange;

}
