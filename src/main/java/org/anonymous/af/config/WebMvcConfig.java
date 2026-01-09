package org.anonymous.af.config;

import org.anonymous.af.interceptor.LogInterceptor;
import org.anonymous.af.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置：注册拦截器
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private LogInterceptor logInterceptor;
    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册日志拦截器，拦截所有请求
        registry.addInterceptor(logInterceptor);
        // 注册令牌校验拦截器，拦截所有请求（排除登录注册接口）
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**") // 拦截所有路径
                .excludePathPatterns(
                        "/user/login",
                        "/user/register"
                );
    }
}