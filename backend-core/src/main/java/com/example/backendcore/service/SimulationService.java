package com.example.backendcore.service;

import com.example.backendcore.entity.Device;
import com.example.backendcore.entity.EnergyData;
import com.example.backendcore.event.EnergyDataEvent;
import com.example.backendcore.mapper.DeviceMapper;
import com.example.backendcore.mapper.EnergyMapper;
import com.example.backendcore.strategy.SimulationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 数据模拟器核心服务
 * 负责定时生成能耗数据，并根据策略注入故障
 */
@Service
public class SimulationService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private EnergyMapper energyMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    // 自动注入所有策略 (Key是Bean的名字, 如 "normalStrategy")
    @Autowired
    private Map<String, SimulationStrategy> strategyMap;

    private final Random random = new Random();

    // 计数器：记录当前已经正常模拟了多少次
    private int currentCounter = 0;

    // 下一次触发故障的目标次数 (正式环境初始化为正常范围)
    private int nextAnomalyTarget = 20;

    // 每 5 秒执行一次模拟
    @Scheduled(fixedRate = 5000)
    public void runSimulation() {
        currentCounter++;
        List<Device> devices = deviceMapper.selectAll();

        if (devices.isEmpty()) {
            System.out.println(">>> 暂无设备，跳过模拟");
            return;
        }

        // 1. 判断是否达到触发故障的轮次
        boolean triggerAnomaly = (currentCounter >= nextAnomalyTarget);

        // 记录本轮被选中的“倒霉蛋”索引
        int targetDeviceIndex = -1;

        if (triggerAnomaly) {
            // 重置计数器
            currentCounter = 0;

            // 频率: 每生成约 20-50 条正常数据后，强制生成一条异常数据
            nextAnomalyTarget = 20 + random.nextInt(31);

            System.out.println(">>> [模拟器] 本轮将触发故障！下一次故障将在 " + nextAnomalyTarget + " 次后触发");

            // 随机选一个设备作为故障目标 (修复了总是第1个设备报错的问题)
            targetDeviceIndex = random.nextInt(devices.size());
        }

        for (int i = 0; i < devices.size(); i++) {
            Device device = devices.get(i);
            String strategyKey = "normalStrategy"; // 默认策略

            // 如果本轮是故障轮，且当前设备是被选中的倒霉蛋
            if (triggerAnomaly && i == targetDeviceIndex) {
                // 随机选择异常类型 (修复了只有过载的问题)
                if (random.nextBoolean()) {
                    // 50% 概率：异常A (过载)
                    strategyKey = "overloadStrategy";
                    System.out.println(">>> 💥 注入故障 A (过载) -> 设备: " + device.getName());
                } else {
                    // 50% 概率：异常B (电压不稳)
                    strategyKey = "voltageInstabilityStrategy";
                    System.out.println(">>> ⚡ 注入故障 B (电压) -> 设备: " + device.getName());
                }
            }

            // 获取策略对象 (如果找不到key就用正常策略保底)
            SimulationStrategy strategy = strategyMap.getOrDefault(strategyKey, strategyMap.get("normalStrategy"));

            // 1. 生成数据 (策略模式执行)
            EnergyData data = strategy.generate(device);

            // 2. 存库
            energyMapper.insert(data);

            // 3. 发布事件 (观察者模式：通知监听器去判断是否告警)
            eventPublisher.publishEvent(new EnergyDataEvent(this, data, device));
        }

        System.out.println("--- 模拟完成: " + devices.size() + " 个设备 (进度: " + currentCounter + "/" + nextAnomalyTarget + ") ---");
    }
}