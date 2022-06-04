package com.kt.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GpuResponse {

    @ApiModelProperty(name = "nodeName", value = "NODE 명")
    @JsonProperty("nodeName")
    String nodeName;

    @ApiModelProperty(name = "nodeName", value = "POD 명")
    @JsonProperty("podName")
    String podName;

    @ApiModelProperty(name = "nameSpace", value = "NameSpace 명")
    @JsonProperty("nameSpace")
    String nameSpace;

    @ApiModelProperty(name = "ipAddr", value = "ip 주소")
    @JsonProperty("ipAddr")
    String ipAddr;

    @ApiModelProperty(name = "podAllocated", value = "POD GPU 할당량")
    @JsonProperty("podAllocated")
    Long podAllocated;

    @ApiModelProperty(name = "nodeAllocated", value = "NODE GPU 할당량/(%)")
    @JsonProperty("nodeAllocated")
    String nodeAllocated;

    @ApiModelProperty(name = "nodeTotalGpu", value = "NODE 전체 GPU 용량")
    @JsonProperty("nodeTotalGpu")
    Long nodeTotalGpu;

}
