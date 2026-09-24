package com.obigo.microev.tms.admin.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obigo.microev.tms.admin.exception.EmpyIdOrPasswordException;
import com.obigo.microev.tms.admin.presentation.login.LoginReqDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 로그인 시도시 사용자가 입력한 정보를 AuthenticationToken에 담아 LoginAuthenticationProvider에게 전달
 */
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public LoginAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {


        LoginReqDto LoginReqDto = objectMapper.readValue(request.getInputStream(), LoginReqDto.class);
        if (StringUtils.isEmpty(LoginReqDto.getEmail()) || StringUtils.isEmpty(LoginReqDto.getPassword())) {
            throw new EmpyIdOrPasswordException();
        }


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(LoginReqDto.getEmail(), LoginReqDto.getPassword(), new ArrayList<>());
        return super.getAuthenticationManager().authenticate(authenticationToken);
    }
}
