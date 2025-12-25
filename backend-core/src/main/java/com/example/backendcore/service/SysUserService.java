package com.example.backendcore.service;

import com.example.backendcore.entity.SysUser;

/**
 * 用户业务层接口
 */
public interface SysUserService {

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 生成的 JWT Token
     */
    String login(String username, String password);

    /**
     * 用户注册
     * @param sysUser 用户信息
     * @return 注册成功的用户信息
     */
    SysUser register(SysUser sysUser);

    /**
     * 根据用户名获取用户信息
     */
    SysUser getUserInfo(String username);
}