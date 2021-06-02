package com.modooary.configuration;

import com.modooary.configuration.interceptor.LoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class DiaryConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;

    //모든 다이어리 접근에 인터셉터가 동작하도록 설정
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/diary/**");
    }
}
