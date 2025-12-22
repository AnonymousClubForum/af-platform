package org.anonymous.af.interceptor;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.anonymous.af.model.entity.UserEntity;
import org.anonymous.af.service.UserService;
import org.anonymous.af.utils.UserContextUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器：解析Token，获取用户信息并放入ThreadLocal
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Resource
    private UserService userService;

    /**
     * 请求处理前执行：解析Token，查询用户信息
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从请求头获取Token（实际场景可替换为Cookie、参数等）
        String token = request.getHeader("Authorization");

        // 2. 校验Token
        if (StrUtil.isBlank(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("未登录，请先登录");
            return false;
        }

        // 3. 解析Token获取用户信息
        UserEntity user = userService.getByUsername(token);

        // 4. 将用户信息放入ThreadLocal
        UserContextUtil.setUser(user);
        return true;
    }

    /**
     * 请求处理完成后执行：清除ThreadLocal
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContextUtil.clear(); // 必须清除，避免线程复用导致数据泄漏
    }
}