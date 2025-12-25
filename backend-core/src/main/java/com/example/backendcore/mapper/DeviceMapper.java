package com.example.backendcore.mapper;

import com.example.backendcore.entity.Device;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface DeviceMapper {
    @Select("SELECT * FROM device")
    List<Device> selectAll();

    @Select("SELECT * FROM device WHERE id = #{id}")
    Device selectById(Long id);

    /**
     * 核心业务检查：统计该建筑该房间内，状态为 ONLINE 的设备数量
     */
    @Select("SELECT COUNT(*) FROM device WHERE building_id = #{buildingId} AND room_no = #{roomNo} AND status = 'ONLINE'")
    int countActiveDevices(@Param("buildingId") Long buildingId, @Param("roomNo") String roomNo);

    /**
     * 新增设备
     */
    @Insert("INSERT INTO device (name, sn, building_id, room_no, p_max, status, create_time) VALUES (#{name}, #{sn}, #{buildingId}, #{roomNo}, #{pMax}, 'ONLINE', NOW())")
    int insert(Device device);
}