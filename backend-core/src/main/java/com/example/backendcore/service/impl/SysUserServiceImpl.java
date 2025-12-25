package com.example.backendcore.service.impl;

import com.example.backendcore.entity.SysUser;
import com.example.backendcore.mapper.SysUserMapper;
import com.example.backendcore.service.SysUserService;
import com.example.backendcore.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public String login(String username, String password) {
        // 1. 根据用户名查询用户
        SysUser user = sysUserMapper.selectByUsername(username);

        // 2. 判断用户是否存在
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 3. 校验密码 (注意：这里暂时用明文比对，生产环境请用 BCrypt 加密)
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }

        // 4. 生成 Token (将用户名和角色存入 Token)
        // 假设数据库里 role 字段存的是 "admin" 或 "user"
        String token = JwtUtils.generateToken(user.getUsername(), user.getRole());

        return token;
    }

    @Override
    public SysUser register(SysUser sysUser) {
        // 1. 校验用户名是否已存在
        SysUser existUser = sysUserMapper.selectByUsername(sysUser.getUsername());
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 2. 设置默认值
        // 如果前端没传角色，默认为普通用户
        if (!StringUtils.hasLength(sysUser.getRole())) {
            sysUser.setRole("user");
        }
        // 设置创建时间
        sysUser.setCreateTime(LocalDateTime.now());

        // 3. 保存到数据库
        // TODO: 生产环境这里应该对 sysUser.getPassword() 进行加密后再存
        sysUserMapper.insert(sysUser);

        return sysUser;
    }

    @Override
    public SysUser getUserInfo(String username) {
        return sysUserMapper.selectByUsername(username);
    }
}