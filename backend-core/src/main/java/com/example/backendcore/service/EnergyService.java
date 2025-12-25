package com.example.backendcore.service;

import com.example.backendcore.entity.EnergyData;
import java.util.Map;

public interface EnergyService {

    /**
     * 获取指定设备的图表数据
     * 返回格式包含 x轴(时间) 和 y轴(功率) 数据，方便前端 ECharts 使用
     * @param deviceId 设备ID
     * @return Map包含了 "categories" (时间列表) 和 "series" (数值列表)
     */
    Map<String, Object> getChartData(Long deviceId);
}