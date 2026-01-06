package org.anonymous.af.utils;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

// JWT工具类
@Component
public class JwtUtil {
    // 签名密钥（生产环境需配置在配置文件，且足够复杂，建议至少32位）
    @Value("${jwt.secret:af-32-bit-secret-key-anonymous}")
    private static String secret;

    // Token过期时间（2小时，单位：毫秒）
    @Value("${jwt.expire-time:7200000}")
    private static long expireTime;

    /**
     * 生成JWT Token
     *
     * @param userId   用户ID
     * @param username 用户名
     * @return JWT Token
     */
    public static String generateToken(Long userId, String username) {
        // 过期时间 = 当前时间 + 过期时长
        Date expireDate = new Date(System.currentTimeMillis() + expireTime);

        // 生成Token
        return Jwts.builder()
                .setId(userId.toString())           // 设置载荷
                .setSubject(username)
                .setIssuedAt(new Date())            // 设置签发时间
                .setExpiration(expireDate)          // 设置过期时间
                .signWith(SignatureAlgorithm.HS256, secret) // 签名算法+密钥
                .compact(); // 生成最终Token
    }

    /**
     * 解析Token，获取载荷中的用户信息
     *
     * @param token JWT Token
     * @return 载荷中的用户信息（Map）
     */
    public static Claims parseToken(String token) {
        try {
            // 解析Token并验证签名、过期时间
            return Jwts.parser()
                    .setSigningKey(secret) // 设置验证密钥
                    .parseClaimsJws(token)         // 解析Token
                    .getBody();                    // 获取载荷
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token已过期");
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new RuntimeException("Token无效或被篡改");
        }
    }
}