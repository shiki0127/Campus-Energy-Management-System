package com.example.backendcore.mapper;

import com.example.backendcore.entity.Building;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface BuildingMapper {
    /**
     * 查询所有建筑信息
     */
    @Select("SELECT * FROM building ORDER BY id ASC")
    List<Building> selectAll();

    /**
     * 根据ID查询
     */
    @Select("SELECT * FROM building WHERE id = #{id}")
    Building selectById(Long id);
}