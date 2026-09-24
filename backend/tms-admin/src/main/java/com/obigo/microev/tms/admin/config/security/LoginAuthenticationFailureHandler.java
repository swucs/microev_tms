package com.obigo.microev.tms.admin.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obigo.microev.tms.admin.exception.CustomCredentialException;
import com.obigo.microev.tms.admin.presentation.ResponseDto;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 로그인 실패 처리 Handler
 */
@RequiredArgsConstructor
@Configuration
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        ResponseDto<String> result = new ResponseDto<>();

        if (e instanceof CustomCredentialException) {
            //비밀번호가 만료된 경우 Error Code 응답
            result.setResultCode(((CustomCredentialException) e).getErrorCode());
            result.setResultMessage(e.getMessage());
        } else {
            //그 외의 경우 Error Code 응답
            result.setResultCode(ResponseCode.SERVER_EXCEPTION.getCode());
            result.setResultMessage(ResponseCode.SERVER_EXCEPTION.getMessage());
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        OutputStream outputStream = response.getOutputStream();
        objectMapper.writeValue(outputStream, result);
    }
}
