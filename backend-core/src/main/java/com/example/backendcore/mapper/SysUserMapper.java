package com.example.backendcore.mapper;

import com.example.backendcore.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 用户持久层接口
 */
@Mapper
public interface SysUserMapper {

    /**
     * 新增用户
     */
    int insert(SysUser sysUser);

    /**
     * 根据用户名查询用户（登录核心逻辑）
     */
    SysUser selectByUsername(@Param("username") String username);

    /**
     * 根据ID查询
     */
    SysUser selectById(@Param("id") Long id);

    /**
     * 查询所有用户
     */
    List<SysUser> selectAll();
}