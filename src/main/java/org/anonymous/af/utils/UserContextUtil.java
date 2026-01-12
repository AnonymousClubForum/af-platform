package org.anonymous.af.utils;

import jakarta.validation.constraints.NotNull;

/**
 * 用户上下文工具类，存储当前线程的登录用户信息
 */
public class UserContextUtil {

    // 定义ThreadLocal，存储LoginUser对象
    private static final ThreadLocal<Long> USER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置当前线程的用户信息
     */
    public static void setUserId(@NotNull Long userId) {
        USER_THREAD_LOCAL.set(userId);
    }

    /**
     * 获取当前线程的用户信息
     */
    @NotNull
    public static Long getUserId() {
        return USER_THREAD_LOCAL.get();
    }

    /**
     * 清除当前线程的用户信息（关键：避免内存泄漏）
     */
    public static void clear() {
        USER_THREAD_LOCAL.remove();
    }
}
