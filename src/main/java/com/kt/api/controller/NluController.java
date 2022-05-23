package com.kt.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.api.config.RabbitMqConfiguration;
import com.kt.api.model.DefaultResponse;
import com.kt.api.model.MQRequest;
import com.kt.api.model.StatusEnum;
import com.kt.api.util.ResponseUtils;
import com.rabbitmq.client.Channel;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("nlu")
@Api("nlu execute service")
public class NluController {
    @Autowired
    @Qualifier("mapper")
    ObjectMapper mapper;

    @RequestMapping(value = "/train", method = RequestMethod.POST)
    public ResponseEntity<DefaultResponse> nluTrain(@RequestBody @Valid MQRequest request)  {
        /**
         * 1. log 출력
         * 2. history 저장
         *         ActionLogging logging = ActionLogging.builder()
         *                 .actionName(command.getName())
         *                 .serviceType("GLUE")
         *                 .ipAddress(request.getRemoteHost())
         *                 .requestId(requestId)
         *                 .username(context.getUsername()).build();
         *         actionLoggingService.startUsage(logging);
         *
         *         context.setLogging(logging);
         * 3. sendMessage 구현
         *    RabbitController의 sendMessage 메소드 참고
         *
         */
        return ResponseUtils.getResponse(StatusEnum.OK,"Send Message Success!","Send Message Success!");
    }
}
