package org.anonymous.af.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 日志拦截器：打印请求信息
 */
@Component
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    /**
     * 请求处理前执行：打印请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("URL:{} Method:{}", request.getRequestURL().toString(), request.getMethod());

        return true;
    }

    /**
     * 请求处理完成后执行：清除ThreadLocal
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("URL:{} Method:{} Status:{}", request.getRequestURL().toString(), request.getMethod(), response.getStatus());
    }
}