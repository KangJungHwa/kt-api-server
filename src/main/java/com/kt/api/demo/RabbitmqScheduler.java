package com.kt.api.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

//@Service
@Slf4j
public class RabbitmqScheduler {
    private final RabbitListenerEndpointRegistry registry;
        public RabbitmqScheduler(RabbitListenerEndpointRegistry registry) {
        this.registry = registry;
    }
      //  @Scheduled(cron = "5 * * * * *")
    public void printQueue() {
        //registry.getListenerContainers().
        SimpleMessageListenerContainer container =(SimpleMessageListenerContainer) registry.getListenerContainer("nlu-topic-exchange");
//        MessageListenerContainer container = rabbitConfiguration.messageListenerContainer(rabbitMqConfiguration);
        String[] queueNames = container.getQueueNames();
        for (String q:queueNames) {
            System.out.println("q:"+q);
        }
    }
}
