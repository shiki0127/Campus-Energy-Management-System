package com.example.backendcore.service;

import com.example.backendcore.entity.Alarm;
import com.example.backendcore.entity.Building;
import com.example.backendcore.entity.Device;
import java.util.List;

public interface CampusService {
    List<Building> getBuildingList();
    List<Device> getDeviceList();
    List<Alarm> getAlarmList();

    void addDevice(Device device);
}