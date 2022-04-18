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
@Entity(name = "gpu_status")
@IdClass(GpuEntityPK.class)
public class GpuEntity implements Serializable {

    @Id
    @Column(name = "node_name", columnDefinition = "VARCHAR(100)", nullable = true)
    private String nodename;
    /**
     * @CreationTimestamp를 키로 사용할 수 없다.
     * 생성일 (이 필드에는 값을 입력하지 않아도 Hibernate가 INSERT시 자동으로 기록)
     */
    @Id
    @Column(name = "create_ts")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createDate;

    @Column(name = "memory_unit")
    String memoryUnit;

    @Column(name = "fb_memory_total")
    Long fbMemoryTotal;

    @Column(name = "fb_memory_used")
    Long fbMemoryUsed;

    @Column(name = "fb_memory_free")
    Long fbMemoryFree;

    @Column(name = "util_unit")
    String utilUnit;

    @Column(name = "gpu_util")
    Long gpuUtil;

    @Column(name = "memory_util")
    Long memoryUtil;

    @Column(name = "encoder_util")
    Long encoderUtil;

    @Column(name = "decoder_util")
    Long decoderUtil;

}
