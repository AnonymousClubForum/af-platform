package org.anonymous.af.interceptor;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.anonymous.af.constants.ResponseConstants;
import org.anonymous.af.model.UserContext;
import org.anonymous.af.utils.JwtUtil;
import org.anonymous.af.utils.UserContextUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * 令牌校验拦截器：解析Token，获取用户信息并放入ThreadLocal
 */
@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {
    @Resource
    private JwtUtil jwtUtil;

    /**
     * 请求处理前执行：解析Token，查询用户信息
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setCharacterEncoding("UTF-8");
        // 获取请求头中的Token
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)) {
            // 未携带Token，返回401
            response.setStatus(ResponseConstants.UNAUTHORIZED);
            try {
                response.getWriter().write("未登录，请先登录");
            } catch (IOException ignore) {
            }
            return true;
        }

        // 验证并解析JWT Token
        try {
            // 从载荷中获取用户ID校验
            UserContext user = new UserContext();
            user.setId(Long.parseLong(jwtUtil.parseIdFromToken(token)));
        } catch (Exception e) {
            // Token过期/无效，返回401
            response.setStatus(401);
            try {
                response.getWriter().write(e.getMessage());
            } catch (IOException ignore) {
            }
            return true;
        }

        return true; // 放行请求
    }

    /**
     * 请求处理完成后执行：清除ThreadLocal
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContextUtil.clear(); // 必须清除，避免线程复用导致数据泄漏
    }
}