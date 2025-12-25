package com.example.backendcore.controller;

import com.example.backendcore.common.Result;
import com.example.backendcore.entity.SysUser;
import com.example.backendcore.service.SysUserService;
import com.example.backendcore.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户接口控制器
 * 前端访问路径: http://localhost:8080/user/xxx
 */
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 登录接口
     * POST /user/login
     * 参数: {"username": "admin", "password": "123"}
     */
    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody SysUser sysUser) {
        // 1. 检查参数
        String username = sysUser.getUsername();
        String password = sysUser.getPassword();
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            return Result.error("用户名或密码不能为空");
        }

        // 2. 调用业务层登录，获取 Token
        try {
            String token = sysUserService.login(username, password);

            // 3. 封装返回数据
            Map<String, String> data = new HashMap<>();
            data.put("token", token);
            return Result.success(data);

        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 注册接口
     * POST /user/register
     */
    @PostMapping("/register")
    public Result<SysUser> register(@RequestBody SysUser sysUser) {
        // 简单校验
        if (!StringUtils.hasLength(sysUser.getUsername()) || !StringUtils.hasLength(sysUser.getPassword())) {
            return Result.error("用户名或密码不能为空");
        }

        try {
            SysUser registeredUser = sysUserService.register(sysUser);
            return Result.success(registeredUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取当前用户信息 (需要携带 Token)
     * GET /user/info?token=xxxx
     */
    @GetMapping("/info")
    public Result<SysUser> getUserInfo(@RequestParam("token") String token) {
        // 1. 校验 Token
        if (!StringUtils.hasLength(token) || !JwtUtils.validateToken(token)) {
            return Result.error("Token 无效或已过期，请重新登录");
        }

        // 2. 解析 Token 获取用户名
        String username = JwtUtils.getClaimsByToken(token).getSubject();

        // 3. 查询用户信息
        SysUser user = sysUserService.getUserInfo(username);

        // 脱敏处理（不把密码返回给前端）
        user.setPassword(null);

        return Result.success(user);
    }
}