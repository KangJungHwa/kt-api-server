package com.kt.api.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.api.controller.RestTemplateController;
import com.kt.api.model.entity.NodeEntity;
import com.kt.api.model.nodes.Nodes;
import com.kt.api.repository.NodeRepository;
import com.kt.api.util.SSLUtils;
import com.kt.api.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class NodeResourceInfoTask extends RestTemplateController {
    @Autowired
    @Qualifier("mapper")
    ObjectMapper mapper;

    @Autowired
    NodeRepository nodeRepository;

    @Value("${app.backend.k8s.url}")
    String k8sUrl;

    @Value("${app.backend.k8s.token}")
    String token;

    @Autowired
    @Qualifier("sslRestTemplate")
    RestTemplate restTemplate;



    //1분마다 실행 kube-apiserver 호출
    //http://172.30.1.81:30003/k8s-apis/metrics.k8s.io/v1beta1/nodes

    //@Scheduled(cron="0 * * * * *")
    public void run() throws JsonProcessingException {
        HttpEntity<String> entity = emptyGetRequestEntity(token);
        String url = "http://172.30.1.81:30003/k8s-apis/metrics.k8s.io/v1beta1/nodes";

        ResponseEntity<String> responseEntity= restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        Nodes nodes = mapper.readValue(responseEntity.getBody(), Nodes.class);
        List<NodeEntity> nodeUsageList =
                new ArrayList<>();
        java.sql.Timestamp now = TimeUtil.getNow();
        for(int i=0; i<nodes.getItems().size(); i++) {

            String nodeRole=null;
            if (nodes.getItems().get(i).getMetadata().getLabels().getNodeRole() != null) {
                nodeRole = "master";
            } else {
                nodeRole = "worker";
            }
            String memoryUnit=null;
            String cpu=nodes.getItems().get(i).getUsage().getCpu().replaceAll("n", "");

            String memory=nodes.getItems().get(i).getUsage().getMemory();
            if(memory.indexOf("Ki")>0){
                memory=memory.replaceAll("Ki", "");
            }
            if(memory.indexOf("Mi")>0) {
                memory=memory.replaceAll("Mi", "");
            }
            if(memory.indexOf("Gi")>0) {
                memory=memory.replaceAll("Gi", "");
            }
            NodeEntity nodeEntity=NodeEntity.builder()
                    .nodename(nodes.getItems().get(i).getMetadata().getName())
                    .nodeRole(nodeRole)
                    .cpu(Long.valueOf(cpu))
                    .memory(Long.valueOf(memory))
                    .createDate(now).build();
            nodeUsageList.add(nodeEntity);
        }
        nodeRepository.saveAll(nodeUsageList);
    }
}
