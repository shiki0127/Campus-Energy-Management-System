package com.example.backendcore.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 建筑区域信息实体
 */
@Data
public class Building implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 建筑名称 (如：力行楼) */
    private String name;

    /** 位置编号 */
    private String location;

    /** 建筑用途分类: DORM/TEACHING/LAB */
    private String type;

    /** 楼层数 */
    private Integer floorCount;

    /** 创建时间 */
    private LocalDateTime createTime;
}