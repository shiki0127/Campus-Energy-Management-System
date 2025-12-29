package com.example.backendcore.strategy.impl;

import com.example.backendcore.entity.Device;
import com.example.backendcore.entity.EnergyData;
import com.example.backendcore.strategy.SimulationStrategy;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 过载异常策略
 * 功率飙升至 1.2 倍
 */
@Component("overloadStrategy")
public class OverloadStrategy implements SimulationStrategy {
    @Override
    public EnergyData generate(Device device) {
        double voltage = 220.0;
        double power = device.getPMax().doubleValue() * 1.2; // 1.2倍过载
        double current = power / voltage;

        return EnergyData.builder()
                .deviceId(device.getId())
                .voltage(BigDecimal.valueOf(voltage))
                .power(BigDecimal.valueOf(power))
                .current(BigDecimal.valueOf(current))
                .kwh(BigDecimal.ZERO)
                .createTime(LocalDateTime.now())
                .build();
    }
}