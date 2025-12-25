package com.example.backendcore.listener;

import com.example.backendcore.entity.Alarm;
import com.example.backendcore.entity.Device;
import com.example.backendcore.entity.EnergyData;
import com.example.backendcore.event.EnergyDataEvent;
import com.example.backendcore.mapper.AlarmMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AlarmEventListener {

    @Autowired
    private AlarmMapper alarmMapper;

    @Async
    @EventListener
    public void handleEnergyEvent(EnergyDataEvent event) {
        EnergyData data = event.getEnergyData();
        Device device = event.getDevice();

        // 1. 监控标准一：功率过载 (超过额定值)
        if (data.getPower().compareTo(device.getPMax()) > 0) {
            System.out.println(">>> ⚠️ 警告: " + device.getName() + " 过载! 当前:" + data.getPower() + "W");

            Alarm alarm = Alarm.builder()
                    .deviceId(device.getId())
                    .type("OVERLOAD")
                    .value(data.getPower())
                    .detail("功率过载，超过阈值 " + device.getPMax())
                    .build();
            alarmMapper.insert(alarm);
        }

        // 2. 监控标准二：电压异常 (偏离 220V 超过 ±10%)
        // 阈值下限: 220 * 0.9 = 198V
        // 阈值上限: 220 * 1.1 = 242V
        double voltage = data.getVoltage().doubleValue();
        if (voltage < 198.0 || voltage > 242.0) {
            System.out.println(">>> ⚡ 危险: " + device.getName() + " 电压异常! 当前:" + voltage + "V");

            Alarm alarm = Alarm.builder()
                    .deviceId(device.getId())
                    .type("VOLTAGE_ERR")
                    .value(data.getVoltage())
                    .detail("电压异常波动(±10%)，当前值: " + voltage + "V")
                    .build();
            alarmMapper.insert(alarm);
        }
    }
}