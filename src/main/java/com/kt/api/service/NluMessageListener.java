package com.kt.api.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NluMessageListener {

    @RabbitListener(queues = "nlu.queue")
    public void receiveMessage(final Message message) {
        //여기서 쏘베텍 rest api 호출 로직 삽입
        System.out.println(message);
    }

}
