package com.example.backendcore.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Device implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String sn;
    private Long buildingId;
    private String roomNo;

    // 强制 JSON 序列化为 "pMax"
    @JsonProperty("pMax")
    private BigDecimal pMax;

    private String status;
    private LocalDateTime createTime;
}