package com.example.backendcore.event;

import com.example.backendcore.entity.Device;
import com.example.backendcore.entity.EnergyData;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 定义事件：当采集到数据时发布
 */
@Getter
public class EnergyDataEvent extends ApplicationEvent {
    private final EnergyData energyData;
    private final Device device;

    public EnergyDataEvent(Object source, EnergyData energyData, Device device) {
        super(source);
        this.energyData = energyData;
        this.device = device;
    }
}