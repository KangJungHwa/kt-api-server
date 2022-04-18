package com.kt.api.model.nodes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kt.api.model.nodes.Item;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Nodes {
    @JsonProperty("kind")
    String kind;
    @JsonProperty("apiVersion")
    String apiVersion;
    @JsonProperty("items")
    List<Item> items;

}
