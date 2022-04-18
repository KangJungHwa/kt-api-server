package com.kt.api.schedule;

import com.kt.api.model.entity.GpuEntity;
import com.kt.api.model.entity.NetworkEntity;
import com.kt.api.repository.GpuRepository;
import com.kt.api.util.EncryptionUtils;
import com.kt.api.util.SSHUtils;
import com.kt.api.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@ConfigurationProperties(prefix = "k8s")
public class GpuInfoTask {

    @Autowired
    GpuRepository gpuRepository;
    private Map<String, String> gpunodes;
    private Map<String, Integer> gpuports;

    private String username;
    private String password;
    public void setUsername(final String username) {this.username = username;}
    public void setPassword(final String password) {this.password = password;}
    public void setGpunodes(final Map<String, String> gpunodes) {this.gpunodes = gpunodes;}
    public void setGpuports(final Map<String, Integer> gpuports) {this.gpuports = gpuports;}
//    //아래 명령어를 수행하는 shell을 1분마다 실행
//    //for i in {3..4} ; do echo 192.168.0.5${i} ; done | xargs -P 1 -I {} ssh {} "nvidia-smi -q -x"

    @Scheduled(cron="0 * * * * *")
    public void run()  throws Exception {
        Timestamp createTimestamp= TimeUtil.getNow();
        for (Object nodename:gpunodes.keySet()) {
             String responseStr=  SSHUtils.getSshResult(username,
                    EncryptionUtils.getDecodingStr(password),
                    gpunodes.get(nodename),
                    "nvidia-smi -q -x",
                    gpuports.get(nodename));
            saveResult(nodename.toString(), responseStr, createTimestamp);
        }
    }


    /**
     * 명령어 실행결과 저장
     * @param nodename 노드명
     * @param responseStr 명령어 실행결과
     */
    public void saveResult(String nodename,String responseStr,Timestamp createTimestamp) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory  =  DocumentBuilderFactory.newInstance();
        factory.setValidating (false);

        DocumentBuilder builder    =  factory.newDocumentBuilder();


        builder.setEntityResolver(new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId)
                    throws SAXException, IOException {

                    return new InputSource(new StringReader(""));

            }
        });

        Document document     =  builder.parse(new InputSource(new StringReader(responseStr)));
        document.getDocumentElement().normalize();

        NodeList memoryList = document.getElementsByTagName("fb_memory_usage");
        NodeList utilList = document.getElementsByTagName("utilization");
        String gpu_util=null;
        String memory_util =null;
        String encoder_util=null;
        String decoder_util =null;
        String total=null;
        String used =null;
        String free =null;

        List<GpuEntity> gpuList = new ArrayList<>();
        for (int temp = 0; temp < memoryList.getLength(); temp++) {

            Node node = memoryList.item(temp);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;

                 total = element.getElementsByTagName("total").item(0).getTextContent();
                 used = element.getElementsByTagName("used").item(0).getTextContent();
                 free = element.getElementsByTagName("free").item(0).getTextContent();
                System.out.println("total : " + total);
                System.out.println("used : " + used);
                System.out.println("free : " + free);
            }
        }
        for (int temp = 0; temp < utilList.getLength(); temp++) {

            Node node = utilList.item(temp);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;

                 gpu_util = element.getElementsByTagName("gpu_util").item(0).getTextContent();
                 memory_util = element.getElementsByTagName("memory_util").item(0).getTextContent();
                 encoder_util = element.getElementsByTagName("encoder_util").item(0).getTextContent();
                 decoder_util = element.getElementsByTagName("decoder_util").item(0).getTextContent();
            }
        }



            GpuEntity gpuEntity = GpuEntity.builder()
                    .nodename(nodename)
                    .createDate(createTimestamp)
                    .memoryUnit("MiB")
                    .fbMemoryTotal(Long.valueOf(total.replaceAll(" MiB","")))
                    .fbMemoryUsed(Long.valueOf(used.replaceAll(" MiB","")))
                    .fbMemoryFree(Long.valueOf(free.replaceAll(" MiB","")))
                    .utilUnit("%")
                    .gpuUtil(Long.valueOf(gpu_util.replaceAll(" %","")))
                    .memoryUtil(Long.valueOf(memory_util.replaceAll(" %","")))
                    .encoderUtil(Long.valueOf(encoder_util.replaceAll(" %","")))
                    .decoderUtil(Long.valueOf(decoder_util.replaceAll(" %","")))
                    .build();
            gpuList.add(gpuEntity);
            gpuRepository.saveAll(gpuList);
        }

}
