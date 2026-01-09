package org.anonymous.af.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

// JWT工具类
@Component
public class JwtUtil {
    // 签名密钥
    @Value("${jwt.secret:af-256-bit-secret-key-anonymous-forum}")
    private String secret;

    // Token过期时间（2小时，单位：毫秒）
    @Value("${jwt.expire-time:7200000}")
    private long expireTime;

    /**
     * 生成JWT Token
     *
     * @param userId 用户ID
     * @return JWT Token
     */
    public String generateToken(Long userId) {
        // 过期时间 = 当前时间 + 过期时长
        Date expireDate = new Date(System.currentTimeMillis() + expireTime);

        // 生成Token
        return Jwts.builder()
                .id(userId.toString())  // 设置载荷
                .issuedAt(new Date())   // 设置签发时间
                .expiration(expireDate) // 设置过期时间
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8))) // 密钥
                .compact(); // 生成最终Token
    }

    /**
     * 解析Token，获取载荷中的用户信息
     *
     * @param token JWT Token
     * @return 载荷中的用户Id
     */
    public String parseIdFromToken(String token) {
        try {
            // 解析Token并验证签名、过期时间
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8))) // 设置验证密钥
                    .build()
                    .parseEncryptedClaims(token)    // 解析Token
                    .getPayload()
                    .getId();                       // 获取载荷
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token已过期");
        } catch (Exception e) {
            throw new RuntimeException("Token无效或被篡改");
        }
    }
}