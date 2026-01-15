package org.anonymous.af.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.anonymous.af.exception.TokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// JWT工具类
@Component
@Slf4j
public class JwtUtil {
    // 签名密钥
    @Value("${jwt.secret}")
    private String secret;

    // Token过期时间（单位：秒）
    @Value("${jwt.expire-time}")
    private Long expireTime;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    private Map<String, Object> generateClaims(Long userId) {
        // 过期时间 = 当前时间 + 过期时长
        Date expireDate = new Date(System.currentTimeMillis() + expireTime * 1000);
        log.info("generateClaims userId: {}, expireDate: {}", userId, expireDate);
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("iss", "monaxia");
        claims.put("exp", expireDate);
        claims.put("aud", "af");
        claims.put("iat", new Date());
        claims.put("jti", UUID.randomUUID().toString());
        return claims;
    }

    /**
     * 生成JWT Token
     *
     * @param userId 用户ID
     * @return JWT Token
     */
    public String generateToken(Long userId) {
        // 生成Token
        return Jwts.builder()
                .claims(generateClaims(userId))  // 设置载荷
                .signWith(getSecretKey()) // 密钥
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
            log.info("parseIdFromToken: {}", token);
            return Jwts.parser()
                    .verifyWith(getSecretKey()) // 设置验证密钥
                    .build()
                    .parseEncryptedClaims(token) // 解析Token
                    .getPayload()
                    .getId(); // 获取载荷
        } catch (ExpiredJwtException e) {
            throw new TokenException("Token已过期");
        } catch (Exception e) {
            throw new TokenException(e.getMessage());
        }
    }
}