package com.obigo.microev.tms.admin.config;


import com.obigo.microev.tms.admin.config.security.LoginAuthenticationFailureHandler;
import com.obigo.microev.tms.admin.config.security.LoginAuthenticationFilter;
import com.obigo.microev.tms.admin.config.security.LoginAuthenticationProvider;
import com.obigo.microev.tms.admin.config.security.LoginAuthenticationSuccessHandler;
import com.obigo.microev.tms.admin.config.security.jwt.JwtFilter;
import com.obigo.microev.tms.admin.config.security.jwt.JwtTokenProvider;
import com.obigo.microev.tms.admin.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.multipart.support.MultipartFilter;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${cors.allowed-headers}")
    private String allowedHeaders;

    @Value("${cors.allowed-methods}")
    private String allowedMethods;

    @Value("${cors.allowed-origin-patterns}")
    private String allowedOriginPatterns;


    private final LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler;

    private final LoginAuthenticationFailureHandler loginAuthenticationFailureHandler;
    private final JwtTokenProvider jwtTokenProvider;

    public static final String[] PERMIT_URLS = {
            "/tms/admin/login",
            "/tms/admin/login/**",
            "/tms/admin/error",
            "/tms/admin/swagger-ui/**",
            "/tms/admin/api-docs/**",
            "/tms/admin/actuator/**",
            "/tms/admin/reissueAccessToken",
            "/tms/admin/health",
    };


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
                .authenticationManager(authenticationManager)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .anyRequest().permitAll()
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .headers(headers -> //https://www.baeldung.com/spring-prevent-xss
                        headers.xssProtection(
                                xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
                        )
                        /* F/E에서 오류발생으로 일단 주석
                        .contentSecurityPolicy(
                                cps -> cps.policyDirectives("script-src 'self'")
                        )*/
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors((cors) ->
                        cors
                                .configurationSource(configurationSource -> {
                                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                                    corsConfiguration.setAllowCredentials(true);
                                    //credential=true 인 경우 allowedOrigins에 * 를 사용할 수 없으므로 setAllowedOriginPatterns 사용
                                    corsConfiguration.setAllowedOriginPatterns(Arrays.stream(StringUtils.split(allowedOriginPatterns, ",")).map(String::trim).toList());
                                    corsConfiguration.setAllowedMethods(Arrays.stream(StringUtils.split(allowedMethods, ",")).map(String::trim).toList());
                                    corsConfiguration.setAllowedHeaders(Arrays.stream(StringUtils.split(allowedHeaders, ",")).map(String::trim).toList());
                                    return corsConfiguration;
                                })
                )
                .addFilterBefore(loginAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter(), LoginAuthenticationFilter.class)
                .addFilterBefore(multipartFilter(), JwtFilter.class)
                ;

        return http.build();
    }


    @Bean
    public LoginAuthenticationProvider loginAuthenticationProvider(LoginService loginService, PasswordEncoder passwordEncoder) {
        return new LoginAuthenticationProvider(loginService, passwordEncoder);
    }


    /**
     * 로그인 처리 필터
     *
     * @return
     * @throws Exception
     */
    public LoginAuthenticationFilter loginAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception {
        LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter();
        loginAuthenticationFilter.setAuthenticationManager(authenticationManager);
        loginAuthenticationFilter.setAuthenticationSuccessHandler(loginAuthenticationSuccessHandler);
        loginAuthenticationFilter.setAuthenticationFailureHandler(loginAuthenticationFailureHandler);
        return loginAuthenticationFilter;
    }

    /**
     * JWT 필터
     * @return
     */
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtTokenProvider);
    }


    @Bean
    public MultipartFilter multipartFilter() {
        return new MultipartFilter();
    }
}
