package com.kt.api.config;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;

/**
 * 사용방법
 * 			MessageProperties msgProperties = new MessageProperties();
 * 			msgProperties.setHeader("students", students);
 * 			MessageConverter messageConverter = new SimpleMessageConverter();
 * 			Message message = messageConverter.toMessage(msg, msgProperties);
 * 			amqpTemplate.send(exchange, "", message);
 */
//@Configuration
public class RabbitMQHeaderConfig {
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
    HeadersExchange headerExchange() {
        return new HeadersExchange("header-exchange");
    }
    @Bean
    Binding primarystudentsBinding(Queue primarystudentsQueue, HeadersExchange exchange) {
        return BindingBuilder.bind(primarystudentsQueue).to(exchange).where("students").matches("primarystudents");
    }

    @Bean
    Binding secondarystudentsBinding(Queue secondarystudentsQueue,HeadersExchange exchange) {
        return BindingBuilder.bind(secondarystudentsQueue).to(exchange).where("students").matches("secondarystudents");
    }

    @Bean
    Binding collegestudentsBinding(Queue collegestudentsQueue, HeadersExchange exchange) {
        return BindingBuilder.bind(collegestudentsQueue).to(exchange).where("students").matches("collegestudents");
    }
}
