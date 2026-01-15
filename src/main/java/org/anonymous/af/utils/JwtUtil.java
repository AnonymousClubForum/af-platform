package org.anonymous.af.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.anonymous.af.exception.TokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

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
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成JWT Token
     *
     * @param userId 用户ID
     * @return JWT Token
     */
    public String generateToken(Long userId) {
        Date expireDate = new Date(System.currentTimeMillis() + expireTime * 1000);
        log.info("generateToken userId: {}, expireDate: {}", userId, expireDate);
        // 生成Token
        return Jwts.builder()
                .claim("id", userId)  // 设置载荷
                .issuer("monaxia")
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(getSecretKey()) // 密钥
                .compact(); // 生成最终Token
    }

    /**
     * 解析Token，获取载荷中的用户信息
     *
     * @param token JWT Token
     * @return 载荷中的用户Id
     */
    public Long parseIdFromToken(String token) {
        try {
            // 解析Token并验证签名、过期时间
            log.info("parseIdFromToken: {}", token);
            return Jwts.parser()
                    .verifyWith(getSecretKey()) // 设置验证密钥
                    .build()
                    .parseSignedClaims(token.trim()) // 解析Token
                    .getPayload()
                    .get("id", Long.class); // 获取载荷
        } catch (ExpiredJwtException e) {
            throw new TokenException("Token已过期，请重新登录");
        } catch (MalformedJwtException e) {
            throw new TokenException("Token格式非法，无法解析");
        } catch (SignatureException e) {
            throw new TokenException("Token签名验证失败，请勿非法篡改");
        } catch (IllegalArgumentException e) {
            throw new TokenException("Token为空或包含非法字符");
        } catch (Exception e) {
            throw new TokenException(e.getMessage());
        }
    }
}