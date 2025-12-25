package com.example.backendcore.mapper;

import com.example.backendcore.entity.Alarm;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface AlarmMapper {
    @Insert("INSERT INTO alarm (device_id, type, value, detail, create_time) VALUES (#{deviceId}, #{type}, #{value}, #{detail}, NOW())")
    int insert(Alarm alarm);

    // 查询列表（保持带 Limit，用于表格展示，防止一次加载几万条卡死）
    List<Map<String, Object>> selectListWithDeviceInfo();

    // 新增：统计总数（用于大屏 KPI，不带 Limit）
    @Select("SELECT COUNT(*) FROM alarm")
    int countAll();
}