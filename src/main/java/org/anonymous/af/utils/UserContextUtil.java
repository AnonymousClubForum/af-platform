package org.anonymous.af.utils;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.anonymous.af.model.entity.UserEntity;

/**
 * 用户上下文工具类，存储当前线程的登录用户信息
 */
@Slf4j
public class UserContextUtil {

    // 定义ThreadLocal，存储LoginUser对象
    private static final ThreadLocal<UserEntity> USER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置当前线程的用户信息
     */
    public static void setUser(@NotNull UserEntity user) {
        USER_THREAD_LOCAL.set(user);
    }

    /**
     * 获取当前线程的用户信息
     */
    @NotNull
    public static UserEntity getUser() {
        return USER_THREAD_LOCAL.get();
    }

    /**
     * 清除当前线程的用户信息（关键：避免内存泄漏）
     */
    public static void clear() {
        USER_THREAD_LOCAL.remove();
    }
}
