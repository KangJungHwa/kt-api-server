package com.kt.api.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;

/**
 * 사용방법 팬아웃은 라우트키가 없다.
 * amqpTemplate.convertAndSend(exchange, "", msg);
*/
//@Configuration
public class RabbitMQFanoutConfig {
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
    FanoutExchange exchange() {
        return new FanoutExchange("fanout-exchange");
    }

    @Bean
    Binding primarystudentsBinding(Queue primarystudentsQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(primarystudentsQueue).to(fanoutExchange);
    }

    @Bean
    Binding secondarystudentsBinding(Queue secondarystudentsQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(secondarystudentsQueue).to(fanoutExchange);
    }

    @Bean
    Binding collegestudentsBinding(Queue collegestudentsQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(collegestudentsQueue).to(fanoutExchange);
    }
}
