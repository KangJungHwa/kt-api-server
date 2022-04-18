package com.kt.api.model.entity;

import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GpuEntityPK implements Serializable {
    private String nodename;
    private Timestamp createDate;
}
