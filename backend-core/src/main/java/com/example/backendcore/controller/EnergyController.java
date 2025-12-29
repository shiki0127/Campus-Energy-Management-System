package com.example.backendcore.controller;

import com.example.backendcore.common.Result;
import com.example.backendcore.mapper.AlarmMapper;
import com.example.backendcore.mapper.EnergyMapper;
import com.example.backendcore.service.EnergyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "能耗数据接口")
@RestController
@RequestMapping("/energy")
public class EnergyController {

    @Autowired
    private EnergyService energyService;

    @Autowired
    private EnergyMapper energyMapper;

    @Autowired
    private AlarmMapper alarmMapper; // 注入 AlarmMapper 以获取准确的告警总数

    @ApiOperation("获取设备实时功率图表数据")
    @GetMapping("/chart")
    public Result<Map<String, Object>> getChartData(@RequestParam("deviceId") Long deviceId) {
        return Result.success(energyService.getChartData(deviceId));
    }

    @ApiOperation("获取能耗排名 Top5")
    @GetMapping("/ranking")
    public Result<List<Map<String, Object>>> getRanking() {
        return Result.success(energyMapper.selectEnergyRanking());
    }

    @ApiOperation("获取大屏核心KPI (实时总功率 + 今日能耗 + 告警总数)")
    @GetMapping("/kpi")
    public Result<Map<String, Object>> getDashboardKPI() {
        Map<String, Object> kpi = new HashMap<>();

        // 1. 实时总功率 (保留1位小数)
        BigDecimal totalPower = energyMapper.selectTotalRealtimePower();
        if (totalPower == null) totalPower = BigDecimal.ZERO;
        kpi.put("totalPower", totalPower.setScale(1, BigDecimal.ROUND_HALF_UP));

        // 2. 今日能耗 (保留1位小数)
        BigDecimal todayEnergy = energyMapper.selectTodayTotalEnergy();
        if (todayEnergy == null) todayEnergy = BigDecimal.ZERO;
        kpi.put("todayEnergy", todayEnergy.setScale(1, BigDecimal.ROUND_HALF_UP));

        // 3. 历史告警总数 (使用 countAll 获取真实总数)
        int alarmCount = alarmMapper.countAll();
        kpi.put("alarmCount", alarmCount);

        return Result.success(kpi);
    }
}