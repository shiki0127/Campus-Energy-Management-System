package com.example.backendcore.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    // 令牌密钥（非常重要，随便写一串复杂的字符串，不要告诉别人）
    private static final String SECRET_KEY = "energy_project_secret_key_2023";

    // 令牌有效期 (这里设为 24小时: 1000 * 60 * 60 * 24)
    private static final long EXPIRATION = 86400000L;

    /**
     * 生成 Token
     * @param username 用户名
     * @param role 用户角色 (admin/user)
     * @return String 类型的 token
     */
    public static String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role); // 把角色存进 Token 里

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username) // 主体是用户名
                .setIssuedAt(new Date()) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // 过期时间
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 签名算法
                .compact();
    }

    /**
     * 解析 Token 获取 Claims (包含所有信息)
     */
    public static Claims getClaimsByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // 解析失败（过期或被篡改）
            return null;
        }
    }

    /**
     * 校验 Token 是否合法
     */
    public static boolean validateToken(String token) {
        return getClaimsByToken(token) != null;
    }
}