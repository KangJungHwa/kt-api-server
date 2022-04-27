package com.kt.api.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "node_resource_usage")
@IdClass(NodeEntityPK.class)
public class NodeEntity implements Serializable {

    @Id
    @Column(name = "node_name", columnDefinition = "VARCHAR(100)", nullable = true)
    private String nodename;

//
//    @Id
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name = "uuid2", strategy = "uuid2")
//    @Column(columnDefinition = "BINARY(16)")
//    private UUID id;

    /**
     * @CreationTimestamp를 키로 사용할 수 없다.
     * 생성일 (이 필드에는 값을 입력하지 않아도 Hibernate가 INSERT시 자동으로 기록)
     */
    @Id
    @Column(name = "create_ts")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createDate;

    @Column(name = "node_role", columnDefinition = "VARCHAR(100)", nullable = true)
    private String nodeRole;

    @Column(name = "cpu")
    Long cpu;

    @Column(name = "cpu_unit", columnDefinition = "VARCHAR(10)", nullable = true)
    String cpuUnit;

    @Column(name = "memory")
    Long memory;

    @Column(name = "memory_unit", columnDefinition = "VARCHAR(10)", nullable = true)
    String memoryUnit;
}
