package com.kt.api.controller;


import com.kt.api.config.RabbitMqConfiguration;
import com.kt.api.model.DefaultResponse;
import com.kt.api.model.MQRequest;
import com.kt.api.model.StatusEnum;


import com.kt.api.model.entity.MessageQueueEntity;
import com.kt.api.repository.MqmappingRepository;
import com.kt.api.util.ApplicationContextHolder;
import com.kt.api.util.ResponseUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rabbitmq")
public class RabbitmqController {


    private  ApplicationContext  context;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
        public ResponseEntity<DefaultResponse> sendMessage(@RequestBody @Valid MQRequest request)  {
        log.info("{}", String.format("'%s' message를 전송합니다.", request.getRequestMessage()));
        log.info("{}", String.format("'%s' routeKey.", request.getRouteKey()));
        try {
            Channel channel = RabbitMqConfiguration.getChannel();
            channel.basicPublish("nlu-topic-exchange", request.getRouteKey(), null, request.getRequestMessage().getBytes());
        }catch (Exception e){
            return ResponseUtils.getResponse(StatusEnum.INTERNAL_SERVER_ERROR,"Send message fail!","Send message fail!");
        }
        return ResponseUtils.getResponse(StatusEnum.OK,"Send Message Success!","Send Message Success!");
    }

    @RequestMapping(value = "/addqueue", method = RequestMethod.POST)
    public ResponseEntity<DefaultResponse> addQueue(@RequestBody List<MessageQueueEntity> requestList) throws IOException {

        try {
            context = ApplicationContextHolder.get();
            MqmappingRepository repo = context.getBean(MqmappingRepository.class);
            SimpleMessageListenerContainer listener = context.getBean("listener", SimpleMessageListenerContainer.class);
            repo.saveAll(requestList);

            for (MessageQueueEntity mq : requestList) {
                RabbitMqConfiguration.getChannel().queueDeclare(mq.getQueueName(), true, false, false, null);
                RabbitMqConfiguration.getChannel().queueBind(mq.getQueueName(), "nlu-topic-exchange", mq.getRouteKey());
                //리시버 등록
                if (mq.equals("receive")) {
                    listener.addQueueNames(mq.getQueueName());
                }
            }
        }catch (Exception e){
            return ResponseUtils.getResponse(StatusEnum.INTERNAL_SERVER_ERROR,"Add queue fail!","Add queue fail!");
        }
        return ResponseUtils.getResponse(StatusEnum.OK,"Add Queue Success!","Add Queue Success!");
    }
    @RequestMapping(value = "/delqueue", method = RequestMethod.POST)
    public ResponseEntity<DefaultResponse> delQueue(@RequestBody List<MessageQueueEntity> requestList) throws IOException {
        try {
            context = ApplicationContextHolder.get();
            MqmappingRepository repo = context.getBean(MqmappingRepository.class);
            for (MessageQueueEntity queue : requestList) {
                repo.delete(queue);
                RabbitMqConfiguration.getChannel().queueUnbind(queue.getQueueName(), "nlu-topic-exchange", queue.getRouteKey());
                RabbitMqConfiguration.getChannel().queueDelete(queue.getQueueName());
            }
        } catch (Exception e) {
            return ResponseUtils.getResponse(StatusEnum.INTERNAL_SERVER_ERROR,"Delete queue fail!","Delete queue fail!");
        }
        return ResponseUtils.getResponse(StatusEnum.OK,"Delete Queue Success!","Delete Queue Success!");
    }

    @RequestMapping(value = "/listqueue", method = RequestMethod.GET)
    public ResponseEntity<DefaultResponse> listQueue() throws IOException {
        List<MessageQueueEntity> mqlist=null;
        try {
            context = ApplicationContextHolder.get();
            MqmappingRepository repo = context.getBean(MqmappingRepository.class);
            mqlist = repo.findAll();
        } catch (Exception e) {
            return ResponseUtils.getResponse(StatusEnum.INTERNAL_SERVER_ERROR,"Queue list search fail!","Queue list search fail!");
        }
        return ResponseUtils.getResponse(StatusEnum.OK,"Queue list search Success!",mqlist);
    }



}
