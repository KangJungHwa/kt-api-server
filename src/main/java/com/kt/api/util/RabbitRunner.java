package com.kt.api.util;


import com.kt.api.model.NluMessage;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//메세지 큐 발생을 위한 테스트 클래스
//@Component
public class RabbitRunner implements CommandLineRunner{

    private static final String topicExchange = "topic.nlu";
    private static final String sendRoutingKey="routekey.send";
    private static final String receiveRoutingKey="routekey.receive";


    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void run(String... args) {
        //아래 방식은 direct Exchange 방식 이다.
        amqpTemplate.convertAndSend(topicExchange, sendRoutingKey, "send second message success~~~");

        System.out.println("Message sent to the RabbitMQ Topic Exchange Successfully") ;

//        amqpTemplate.convertAndSend(topicExchange, receiveRoutingKey, "receive second message success~~~");
//
//        System.out.println("Message receive to the RabbitMQ Topic Exchange Successfully") ;

    }


}
