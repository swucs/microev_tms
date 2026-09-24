package com.obigo.microev.tms.admin.exception;


import com.obigo.microev.tms.admin.service.LoginService;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import org.springframework.security.core.AuthenticationException;

public class BadPasswordException extends AuthenticationException implements CustomCredentialException {
    public BadPasswordException(int failCount) {
        super(String.format(ResponseCode.WRONG_PASSWORD_COUNT.getMessage(), failCount, LoginService.MAX_FAIL_COUNT));
    }


    @Override
    public String getErrorCode() {
        return ResponseCode.WRONG_PASSWORD_COUNT.getCode();
    }
}
