package com.example.backendcore.mapper;

import com.example.backendcore.entity.EnergyData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface EnergyMapper {
    int insert(EnergyData energyData);
    List<EnergyData> selectChartData(@Param("deviceId") Long deviceId);
    List<Map<String, Object>> selectEnergyRanking();

    BigDecimal selectTotalRealtimePower();
    BigDecimal selectTodayTotalEnergy();
}