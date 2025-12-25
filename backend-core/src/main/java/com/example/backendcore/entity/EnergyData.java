package com.example.backendcore.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 升级后的能耗数据
 * 包含电压、电流、功率
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnergyData implements Serializable {
    private Long id;
    private Long deviceId;
    private BigDecimal voltage; // 电压 V
    private BigDecimal current; // 电流 A
    private BigDecimal power;   // 功率 W
    private BigDecimal kwh;     // 累计用电量
    private LocalDateTime createTime;
}