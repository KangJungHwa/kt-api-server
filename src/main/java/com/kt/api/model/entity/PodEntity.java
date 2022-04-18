package com.kt.api.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.sql.Timestamp;


@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "pod_resource_usage")
@IdClass(PodEntityPK.class)
public class PodEntity  implements Serializable {
    @Id
    @Column(name = "pod_name", columnDefinition = "VARCHAR(100)", nullable = true)
    private String podname;

    @Id
    @Column(name = "container_name", columnDefinition = "VARCHAR(100)", nullable = true)
    String containerName;

    @Id
    @Column(name = "create_ts")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createDate;



    @Column(name = "name_space", columnDefinition = "VARCHAR(100)", nullable = true)
    String nameSpace;

    @Column(name = "cpu")
    Long cpu;

    @Column(name = "memory")
    Long memory;
}