package com.kt.api.receiver;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver implements MessageListener {
    public void onMessage(Message message) {
        System.out.println("Consuming Message - " + new String(message.getBody()));
        System.out.println(message.getMessageProperties());
        if(message.getMessageProperties().getReceivedRoutingKey().indexOf("intent.service.result")>0){
            //repository 저장
            //callback url 호출
        }
        if(message.getMessageProperties().getReceivedRoutingKey().indexOf("intent.train.result")>0){
            //repository 저장
            //callback url 호출
        }
        if(message.getMessageProperties().getReceivedRoutingKey().indexOf("ner.service.result")>0){
            //repository 저장
            //callback url 호출
        }
        if(message.getMessageProperties().getReceivedRoutingKey().indexOf("ner.train.result")>0){
            //repository 저장
            //callback url 호출
        }
    }

}
