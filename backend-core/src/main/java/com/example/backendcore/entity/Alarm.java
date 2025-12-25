package com.example.backendcore.entity;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class Alarm {
    private Long id;
    private Long deviceId;
    private String type;      // OVERLOAD, VOLTAGE_ERR
    private BigDecimal value; // 异常值
    private String detail;
    private LocalDateTime createTime;
}