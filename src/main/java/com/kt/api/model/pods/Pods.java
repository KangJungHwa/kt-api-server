package com.kt.api.model.pods;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pods {
    @JsonProperty("kind")
    String kind;
    @JsonProperty("apiVersion")
    String apiVersion;
    @JsonProperty("items")
    List<Item> items;
}
