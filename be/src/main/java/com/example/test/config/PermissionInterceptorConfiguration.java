package com.example.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class PermissionInterceptorConfiguration implements WebMvcConfigurer {
    @Bean
    PermissionInterceptor getPermissionInterceptor() {
        return new PermissionInterceptor();
    }

    @Override
    public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
        String[] whiteList = {
                "/api/v1/auth/account",
                "/api/v1/auth/logout",
                "/api/v1/resumes/by-user",
                "/api/v1/auth/login",
                "/api/v1/auth/register",
                "/api/v1/companies/follow",
                "/api/v1/files/upload",
                "/api/v1/auth/logout",
                "/api/v1/companies/unfollow",
                "/api/v1/auth/refresh",
                "/api/v1/otps",
                "/api/v1/users/password/forgot-password",
                "/api/v1/resumes/by-job",
                "/api/v1/companies",
                "/api/v1/jobs",
                "/api/v1/jobs/get-one/**",
                "/api/v1/companies/get-one/**",
                "/api/v1/statistics/report",

        };

        registry.addInterceptor(getPermissionInterceptor()).excludePathPatterns(whiteList);
    }
}
