package com.example.backendcore.service.impl;

import com.example.backendcore.entity.EnergyData;
import com.example.backendcore.mapper.EnergyMapper;
import com.example.backendcore.service.EnergyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EnergyServiceImpl implements EnergyService {

    @Autowired
    private EnergyMapper energyMapper;

    @Override
    public Map<String, Object> getChartData(Long deviceId) {
        // 1. 调用 Mapper 查询数据库最近的20条数据
        List<EnergyData> dataList = energyMapper.selectChartData(deviceId);

        // 数据库查出来通常是倒序（最新的在最前），为了画图（时间从左到右），我们需要反转一下列表
        Collections.reverse(dataList);

        // 2. 提取 X轴数据 (时间)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        List<String> categories = dataList.stream()
                .map(data -> data.getCreateTime().format(formatter))
                .collect(Collectors.toList());

        // 3. 提取 Y轴数据 (功率 Power) - 你也可以改成展示电压或电流
        List<Double> series = dataList.stream()
                .map(data -> data.getPower().doubleValue())
                .collect(Collectors.toList());

        // 4. 封装成 Map 返回
        Map<String, Object> result = new HashMap<>();
        result.put("categories", categories); // X轴
        result.put("series", series);         // Y轴

        return result;
    }
}