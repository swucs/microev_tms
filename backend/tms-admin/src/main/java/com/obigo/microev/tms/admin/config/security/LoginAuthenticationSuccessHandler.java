package com.obigo.microev.tms.admin.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obigo.microev.tms.core.util.ClientIpUtils;
import com.obigo.microev.tms.admin.presentation.ResponseDto;
import com.obigo.microev.tms.admin.presentation.login.LoginResDto;
import com.obigo.microev.tms.admin.service.LoginService;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 로그인 성공시 Handler
 */
@RequiredArgsConstructor
@Configuration
public class LoginAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final LoginService loginService;

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        //권한(Role)
        AdminDetails adminDetails = (AdminDetails) authentication.getPrincipal();

        if (!response.isCommitted()) {
            //로그인 성공인 경우 (로그인 이력)
            String ipAddress = ClientIpUtils.getUserIP(request);
            LoginResDto loginResDto = loginService.processSuccessfulLogin(adminDetails.getAdminSeq(), ipAddress);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            OutputStream outputStream = response.getOutputStream();
            ResponseDto<LoginResDto> result = ResponseDto.<LoginResDto>builder()
                    .resultCode(ResponseCode.SUCCESS.getCode())
                    .resultMessage(ResponseCode.SUCCESS.getMessage())
                    .data(loginResDto)
                    .build();
            objectMapper.writeValue(outputStream, result);
        }
    }
}
