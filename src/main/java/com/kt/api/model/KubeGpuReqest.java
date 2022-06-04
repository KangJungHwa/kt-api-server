package com.kt.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class KubeGpuReqest {

    @ApiModelProperty(name = "nodeName", value = "노드명")
    @JsonProperty("nodeName")
    String nodeName;

}
