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
@Entity(name = "network_status")
@IdClass(NetworkEntityPK.class)
public class NetworkEntity implements Serializable {

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

    @Column(name = "rx_packets")
    Long rxPackets;

    @Column(name = "rx_bytes")
    Long rxBytes;

    @Column(name = "rx_errors")
    Long rxErrors;

    @Column(name = "rx_human_bytes")
    String rxHumanBytes;

    @Column(name = "tx_packets")
    Long txPackets;

    @Column(name = "tx_bytes")
    Long txBytes;

    @Column(name = "tx_errors")
    Long txErrors;

    @Column(name = "tx_human_bytes")
    String txHumanBytes;

}
