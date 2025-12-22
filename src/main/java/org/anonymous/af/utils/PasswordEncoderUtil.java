package org.anonymous.af.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码加密工具类（BCrypt实现）
 */
public class PasswordEncoderUtil {
    // 计算成本（4-31，默认10，值越大越耗时）
    private static final int ROUNDS = 12;
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(ROUNDS);

    /**
     * 加密密码
     */
    public static String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    /**
     * 验证密码（对比明文和哈希值）
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}