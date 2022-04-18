package com.kt.api.model.entity;

import lombok.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PodEntityPK implements Serializable {
    private String podname;
    private String containerName;
    private Timestamp createDate;
}
