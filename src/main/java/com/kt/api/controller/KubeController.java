package com.kt.api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.JSchException;
import com.kt.api.model.KubeletLogReqest;
import com.kt.api.util.EncryptionUtils;
import com.kt.api.util.SSHUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/kube")
@Api("kubelet 로그 검색 서비스")
@ConfigurationProperties(prefix = "k8s")
public class KubeController {
    @Autowired
    @Qualifier("mapper")
    ObjectMapper mapper;


    private Map<String, String> nodes;
    private Map<String, Integer> ports;

    private String username;
    private String password;


    public void setUsername(final String username) {
        this.username = username;
    }
    public void setPassword(final String password) {
        this.password = password;
    }

    public void setNodes(final Map<String, String> nodes) {
        this.nodes = nodes;
    }
    public void setPorts(final Map<String, Integer> ports) {
        this.ports = ports;
    }


    @ApiOperation("pod 생성")
    @PostMapping("/pods")
    public String createPod(@RequestBody  String body) throws InterruptedException, JSchException {
        log.info("pod를 생성합니다.");
        long startMillis = System.currentTimeMillis();
        log.info("echo \""+body+"\" > /tmp/"+String.valueOf(startMillis)+".yaml;kubectl create -f /tmp/"+String.valueOf(startMillis)+".yaml");
        String responseStr=  SSHUtils.getSshResult(username,
                EncryptionUtils.getDecodingStr(password),
                nodes.get("nlu-framework-master-1"),
               "echo \""+body+"\" > /tmp/"+String.valueOf(startMillis)+".yaml;kubectl create -f "+String.valueOf(startMillis)+".yaml",
                ports.get("nlu-framework-master-1"));

        String endMillis = String.valueOf(System.currentTimeMillis() - startMillis);
        log.info("총 처리 시간은 {}ms 입니다.", endMillis);
        return responseStr;
    }
}
