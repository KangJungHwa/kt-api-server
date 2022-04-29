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
@RequestMapping("/log")
@Api("kubelet 로그 검색 서비스")
@ConfigurationProperties(prefix = "k8s")
public class KubeletLogController {
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


    /**
     curl --request GET \
     --url http://service-api.k8s.io:30083/log/kubelet/ \
     --header 'Content-Type: application/json' \
     --data '{"nodeName":"nlu-framework-worker-1","sinceMinutes":10}'
     * @param request
     * @return
     * @throws InterruptedException
     * @throws JSchException
     */
    @ApiOperation("kubelet 로그 검색")
    @GetMapping("/kubelet")
    public String getKubeletLog(@RequestBody @Valid KubeletLogReqest request) throws InterruptedException, JSchException {
        log.info("{}", String.format("'%s'node의 kubelet 로그를 검색합니다.", request.getNodeName()));
        long startMillis = System.currentTimeMillis();
        String responseStr=  SSHUtils.getSshResult(username,
                EncryptionUtils.getDecodingStr(password),
                nodes.get(request.getNodeName()),
                "sudo journalctl -u kubelet --since \""+ String.valueOf(request.getSinceMinutes()) +" minute ago\" ",
                ports.get(request.getNodeName()));

        String endMillis = String.valueOf(System.currentTimeMillis() - startMillis);
        log.info("총 처리 시간은 {}ms 입니다.", endMillis);
        return responseStr;
    }
}
