package com.kt.api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.JSchException;
import com.kt.api.model.DefaultResponse;
import com.kt.api.model.GpuResponse;
import com.kt.api.model.KubeletLogReqest;
import com.kt.api.model.StatusEnum;
import com.kt.api.util.EncryptionUtils;
import com.kt.api.util.SSHUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.charset.Charset;
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


    @ApiOperation("gpu 사용 pod 조회 ")
    @PostMapping("/gpupods")
    public ResponseEntity<DefaultResponse> createPod(@RequestBody @Valid KubeletLogReqest request) throws InterruptedException, JSchException {
        log.info("gpu 사용 pod를 조회합니다.");
        long startMillis = System.currentTimeMillis();
        DefaultResponse response = new DefaultResponse();
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status;
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {
            String responseStr = SSHUtils.getSshResult(username,
                    EncryptionUtils.getDecodingStr(password),
                    nodes.get("nlu-framework-master-1"),
                    "kubectl inspect gpushare -k",
                    ports.get("nlu-framework-master-1"));

            response.setMessage("Gpu Using Pod Search Success!");
            response.setData(parseJson(responseStr));
             status=HttpStatus.OK;
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setData("Gpu Using Pod Search Failed!");
             status=HttpStatus.INTERNAL_SERVER_ERROR;
        }
        String endMillis = String.valueOf(System.currentTimeMillis() - startMillis);
        log.info("총 처리 시간은 {}ms 입니다.", endMillis);
        return new ResponseEntity<>(response, headers, status);
    }
    public String allTrim(String str){
        return str.replaceAll("^\\s+","").replaceAll("\\s+$","");
    }
    public GpuResponse parseJson(String val){
/*

NAME:       nlu-framework-worker-1
IPADDRESS:  192.168.0.53

NAME              NAMESPACE   GPU0(Allocated)  Pending(Allocated)
gpu-share-pod3    default     11               0
jupyter-admin888  jupyterhub  0                4
Allocated :       15 (31%)
Total :           47
------------------------------------------------------------------------

NAME:       nlu-framework-worker-2
IPADDRESS:  192.168.0.54

NAME            NAMESPACE  GPU0(Allocated)
gpu-share-pod1  default    11
gpu-share-pod2  default    11
Allocated :     22 (46%)
Total :         47
 */
        String nodeName=null;
        String ipAddr=null;
        Long podAllocated=null;
        Long podPendingAllocated=null;
        String  nodeAllocated=null;
        Long nodeTotalGpu=null;

        boolean podStart=false;
        String [] podInfo=null;
        String podName=null;
        String nameSpace=null;
        String[] result = val.split("\n");

        START:  for (int i = 0; i < result.length; i++) {

            if(result[i].startsWith("NAME:")){
                nodeName = result[i].substring(result[i].indexOf("NAME:"));
                nodeName = allTrim(nodeName);
            }
            if(result[i].startsWith("IPADDRESS:")){
                ipAddr = result[i].substring(result[i].indexOf("IPADDRESS:"));
                ipAddr = allTrim(ipAddr);
            }
            if(result[i].indexOf("NAMESPACE")>0){
                podStart=true;
                continue START;
            }
            if(result[i].indexOf("Allocated :")>0){
                podStart=false;
                nodeAllocated=result[i].substring(result[i].indexOf("Allocated : "));
                nodeAllocated = allTrim(nodeAllocated);
            }
            if(result[i].indexOf("Total :")>0){
                podStart=false;
                nodeTotalGpu=Long.parseLong(allTrim(result[i].substring(result[i].indexOf("Total : "))));
            }
            if(podStart){
                podInfo = result[i].split(" ");
                for (int j = 0; j < podInfo.length; j++) {
                    switch (j){
                        case 0:
                           podName = podInfo[j];
                           podName = allTrim(podName);
                           break ;
                        case 1:
                            nameSpace = podInfo[j];
                            nameSpace = allTrim(nameSpace);
                            break ;
                        case 2:
                            podAllocated = Long.parseLong(allTrim(podInfo[j]));;
                            break ;
                        case 4:
                            podPendingAllocated = Long.parseLong(allTrim(podInfo[j]));;
                            break ;
                    }
                }

            }
            podAllocated=podAllocated+podPendingAllocated;
        }
        GpuResponse gpuResponse = GpuResponse.builder()
                .nodeName(nodeName)
                .ipAddr(ipAddr)
                .podName(podName)
                .nameSpace(nameSpace)
                .podAllocated(podAllocated)
                .nodeAllocated(nodeAllocated)
                .nodeTotalGpu(nodeTotalGpu).build();
        return gpuResponse;
    }

}
