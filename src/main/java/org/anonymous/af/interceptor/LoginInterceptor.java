package org.anonymous.af.interceptor;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.anonymous.af.constants.ResponseConstants;
import org.anonymous.af.model.entity.UserEntity;
import org.anonymous.af.utils.JwtUtil;
import org.anonymous.af.utils.UserContextUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器：解析Token，获取用户信息并放入ThreadLocal
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 请求处理前执行：解析Token，查询用户信息
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("UTF-8");
        // 获取请求头中的Token
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)) {
            // 未携带Token，返回401
            response.setStatus(ResponseConstants.UNAUTHORIZED);
            response.getWriter().write("未登录，请先登录");
            return false;
        }

        // 验证并解析JWT Token
        try {
            Claims claims = JwtUtil.parseToken(token);
            // 从载荷中获取用户ID校验
            UserEntity user = UserContextUtil.getUser();
            if (user == null || !user.getId().toString().equals(claims.getId()) || !user.getUsername().equals(claims.getSubject())) {
                response.setStatus(ResponseConstants.UNAUTHORIZED);
                response.getWriter().write("登录信息错误");
                return false;
            }
        } catch (RuntimeException e) {
            // Token过期/无效，返回401
            response.setStatus(401);
            response.getWriter().write(e.getMessage());
            return false;
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