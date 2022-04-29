package com.kt.api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
다이렉트는 queue와 라우트키가 1대1 매핑
mqpTemplate.convertAndSend(exchange, routingKey, msg);
*/
//@Configuration
public class RabbitMQDirectConfig {

    @Bean
    Queue primarystudentsQueue() {
        return new Queue("primarystudentsQueue", false);
    }

    @Bean
    Queue secondarystudentsQueue() {
        return new Queue("secondarystudentsQueue", false);
    }

    @Bean
    Queue collegestudentsQueue() {
        return new Queue("collegestudentsQueue", false);
    }



    @Bean
    DirectExchange exchange() {
        return new DirectExchange("direct-exchange");
    }

    @Bean
    Binding primarystudentsBinding(Queue primarystudentsQueue, DirectExchange exchange) {
        return BindingBuilder.bind(primarystudentsQueue).to(exchange).with("queue.primarystudents");
    }

    @Bean
    Binding secondarystudentsBinding(Queue secondarystudentsQueue, DirectExchange exchange) {
        return BindingBuilder.bind(secondarystudentsQueue).to(exchange).with("queue.secondarystudents");
    }

    @Bean
    Binding collegestudentsBinding(Queue collegestudentsQueue,DirectExchange exchange) {
        return BindingBuilder.bind(collegestudentsQueue).to(exchange).with("queue.collegestudents");
    }
}
