package com.example.backendcore.controller;

import com.example.backendcore.common.Result;
import com.example.backendcore.entity.Alarm;
import com.example.backendcore.entity.Building;
import com.example.backendcore.entity.Device;
import com.example.backendcore.service.CampusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "校园基础信息管理")
@RestController
@RequestMapping("/campus")
public class CampusController {

    @Autowired
    private CampusService campusService;

    @ApiOperation("获取所有建筑列表")
    @GetMapping("/buildings")
    public Result<List<Building>> getBuildings() {
        return Result.success(campusService.getBuildingList());
    }

    @ApiOperation("获取所有设备列表")
    @GetMapping("/devices")
    public Result<List<Device>> getDevices() {
        return Result.success(campusService.getDeviceList());
    }

    @ApiOperation("获取最近告警记录")
    @GetMapping("/alarms")
    public Result<List<Alarm>> getAlarms() {
        return Result.success(campusService.getAlarmList());
    }

    @ApiOperation("新增设备 (含一房一表校验)")
    @PostMapping("/device/add")
    public Result<String> addDevice(@RequestBody Device device) {
        try {
            campusService.addDevice(device);
            return Result.success("设备绑定成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage()); // 返回业务校验失败信息
        }
    }
}