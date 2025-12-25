package com.example.backendcore.service.impl;

import com.example.backendcore.entity.Alarm;
import com.example.backendcore.entity.Building;
import com.example.backendcore.entity.Device;
import com.example.backendcore.mapper.AlarmMapper;
import com.example.backendcore.mapper.BuildingMapper;
import com.example.backendcore.mapper.DeviceMapper;
import com.example.backendcore.service.CampusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampusServiceImpl implements CampusService {

    @Autowired
    private BuildingMapper buildingMapper;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private AlarmMapper alarmMapper;

    @Override
    public List<Building> getBuildingList() {
        return buildingMapper.selectAll();
    }

    @Override
    public List<Device> getDeviceList() {
        return deviceMapper.selectAll();
    }

    @Override
    public List getAlarmList() {
        // 调用关联查询，返回包含设备名的 Map 列表
        return alarmMapper.selectListWithDeviceInfo();
    }

    @Override
    public void addDevice(Device device) {
        int count = deviceMapper.countActiveDevices(device.getBuildingId(), device.getRoomNo());
        if (count > 0) {
            throw new RuntimeException("违反业务规则：该房间 [" + device.getRoomNo() + "] 已绑定有效电表，禁止重复绑定！");
        }
        deviceMapper.insert(device);
    }
}