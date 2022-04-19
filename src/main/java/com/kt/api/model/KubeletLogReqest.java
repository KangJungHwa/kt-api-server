package com.kt.api.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class KubeletLogReqest {
    String nodeName;
    Long sinceMinutes;
}
