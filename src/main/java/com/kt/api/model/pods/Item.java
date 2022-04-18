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
public class Item {
    @JsonProperty("metadata")
    Metadata metadata;
    @JsonProperty("containers")
    List<Container> containers;
}
