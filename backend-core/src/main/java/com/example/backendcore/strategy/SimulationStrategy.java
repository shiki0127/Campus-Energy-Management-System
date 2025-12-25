package com.example.backendcore.strategy;

import com.example.backendcore.entity.Device;
import com.example.backendcore.entity.EnergyData;

public interface SimulationStrategy {
    /**
     * 根据设备参数生成一条模拟数据
     */
    EnergyData generate(Device device);
}