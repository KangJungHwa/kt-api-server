package com.kt.api.schedule;

import com.kt.api.model.entity.NetworkEntity;
import com.kt.api.repository.NetworkRepository;

import com.kt.api.util.EncryptionUtils;
import com.kt.api.util.FileUtils;
import com.kt.api.util.SSHUtils;
import com.kt.api.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@ConfigurationProperties(prefix = "k8s")
public class NodeNetworkPacketInfoTask  {

    @Autowired
    NetworkRepository networkRepository;
    private Map<String, String> nodes;
    private Map<String, Integer> ports;

    private String username;
    private String password;
    private String networkcard;

    public void setNetworkcard(final String networkcard) {
        this.networkcard = networkcard;
    }
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

    @Scheduled(cron="0 * * * * *")
    public void run() throws Exception {

        for (Object nodename:nodes.keySet()) {
           String responseStr=  SSHUtils.getSshResult(username,
                    EncryptionUtils.getDecodingStr(password),
                    nodes.get(nodename),
                    "ifconfig",
                     ports.get(nodename));
            saveResult(nodename.toString(), responseStr,networkcard);
        }
    }

    /**
     * 명령어 실행결과 저장
     * @param nodename 노드명
     * @param responseStr 명령어 실행결과
     */
    public void saveResult(String nodename,String responseStr,String networkcard){
        String[] result = responseStr.split("\n");
        boolean isCheckStart=false;
        Long rxPackets=0L;
        Long rxBytes=0L;
        Long rxErrors=0L;
        String rxHumanBytes=null;
        Long txPackets=0L;
        Long txBytes=0L;
        Long txErrors=0L;
        String txHumanBytes=null;
        List<NetworkEntity> networkList = new ArrayList<>();
        Timestamp createTimestamp=TimeUtil.getNow();
        for (int i = 0; i < result.length; i++) {
            String line=result[i].trim();
            if(line.startsWith(networkcard)){
                isCheckStart=true;
            }
            if(isCheckStart && line.startsWith("RX packets")){
                rxPackets=Long.valueOf(result[i].substring(result[i].indexOf("packets")+8,result[i].indexOf("bytes")-2));
                rxBytes=Long.valueOf(result[i].substring(result[i].indexOf("bytes")+6,result[i].indexOf(" (")));
                rxHumanBytes= FileUtils.humanReadableByteCountBin(rxBytes);
            }
            if(isCheckStart && line.startsWith("RX errors")){
                rxErrors=Long.valueOf(result[i].substring(result[i].indexOf("errors")+7,result[i].indexOf("dropped")-2));
            }
            if(isCheckStart && line.startsWith("TX packets")){
                txPackets=Long.valueOf(result[i].substring(result[i].indexOf("packets")+8,result[i].indexOf("bytes")-2));
                txBytes=Long.valueOf(result[i].substring(result[i].indexOf("bytes")+6,result[i].indexOf(" (")));
                txHumanBytes= FileUtils.humanReadableByteCountBin(txBytes);
            }
            if(isCheckStart && line.startsWith("TX errors")){
                txErrors=Long.valueOf(result[i].substring(result[i].indexOf("errors")+7,result[i].indexOf("dropped")-2));
                break;
            }
            NetworkEntity networkEntity = NetworkEntity.builder()
                    .rxPackets(rxPackets)
                    .rxBytes(rxBytes)
                    .rxHumanBytes(rxHumanBytes)
                    .rxErrors(rxErrors)
                    .txPackets(txPackets)
                    .txBytes(txBytes)
                    .txHumanBytes(txHumanBytes)
                    .txErrors(txErrors)
                    .nodename(nodename)
                    .createDate(createTimestamp).build();
            networkList.add(networkEntity);
        }
        networkRepository.saveAll(networkList);
    }
}
