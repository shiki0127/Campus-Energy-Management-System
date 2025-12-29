package com.example.backendcore.strategy.impl;

import com.example.backendcore.entity.Device;
import com.example.backendcore.entity.EnergyData;
import com.example.backendcore.strategy.SimulationStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * 异常类型 B: 电压不稳
 * 电压突然跌至 180V 或升至 260V
 */
@Component("voltageInstabilityStrategy")
public class VoltageInstabilityStrategy implements SimulationStrategy {

    private final Random random = new Random();

    @Override
    public EnergyData generate(Device device) {
        // 随机选择 180V (低压) 或 260V (高压)
        double voltage = random.nextBoolean() ? 180.0 : 260.0;

        // 功率保持正常范围或略微波动，这里假设电器阻抗不变，简单计算
        // 假设设备处于平均负载 500W
        double power = 500.0;

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