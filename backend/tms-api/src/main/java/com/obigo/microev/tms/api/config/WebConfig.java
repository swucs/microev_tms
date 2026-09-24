package com.obigo.microev.tms.api.config;

import com.obigo.microev.tms.api.interceptor.JwtTokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final LocaleConfig messageConfig;
    private final JwtTokenInterceptor jwtTokenInterceptor;


    @Value("${cors.allowed-headers}")
    private String allowedHeaders;

    @Value("${cors.allowed-methods}")
    private String allowedMethods;

    @Value("${cors.allowed-origin-patterns}")
    private String allowedOriginPatterns;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(Arrays.stream(StringUtils.split(allowedOriginPatterns, ",")).map(String::trim).toArray(String[]::new))// allowedOriginPatterns 사용
                .allowedMethods(Arrays.stream(StringUtils.split(allowedMethods, ",")).map(String::trim).toArray(String[]::new))
                .allowedHeaders(Arrays.stream(StringUtils.split(allowedHeaders, ",")).map(String::trim).toArray(String[]::new))
                .allowCredentials(true)
        ;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(messageConfig.localeChangeInterceptor());


        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/static/**", "/css/**", "/images/**", "/js/**", "/fonts/**"
                        , "/auth/login"
                        , "/swagger-ui/**"
                        , "/api-docs/**"
                );
    }

}
