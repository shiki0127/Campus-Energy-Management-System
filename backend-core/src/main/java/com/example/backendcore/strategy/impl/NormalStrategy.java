package com.example.backendcore.strategy.impl;

import com.example.backendcore.entity.Device;
import com.example.backendcore.entity.EnergyData;
import com.example.backendcore.strategy.SimulationStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * 正常模式策略
 * 电压在 220V 附近波动，功率在 0.2-0.9 倍额定功率之间
 */
@Component("normalStrategy")
public class NormalStrategy implements SimulationStrategy {
    private final Random random = new Random();

    @Override
    public EnergyData generate(Device device) {
        // 电压 215 - 225V
        double voltage = 215 + random.nextDouble() * 10;

        // 功率: 20% - 90% 的额定功率
        double pMax = device.getPMax().doubleValue();
        double power = pMax * (0.2 + random.nextDouble() * 0.7);

        // 计算电流 I = P / U
        double current = power / voltage;

        return EnergyData.builder()
                .deviceId(device.getId())
                .voltage(BigDecimal.valueOf(voltage))
                .power(BigDecimal.valueOf(power))
                .current(BigDecimal.valueOf(current))
                .kwh(BigDecimal.ZERO) // 累计量暂不模拟
                .createTime(LocalDateTime.now())
                .build();
    }
}