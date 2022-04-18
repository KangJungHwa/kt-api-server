package com.kt.api.model.nodes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    @JsonProperty("metadata")
    Metadata metadata;
    @JsonProperty("timestamp")
    String timestamp;
    @JsonProperty("usage")
    Usage usage;
}
