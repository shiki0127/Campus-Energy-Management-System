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

    // 查询列表
    List<Map<String, Object>> selectListWithDeviceInfo();

    // 统计总数
    @Select("SELECT COUNT(*) FROM alarm")
    int countAll();
}