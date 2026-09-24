package com.obigo.microev.tms.admin.exception;


import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import org.springframework.security.core.AuthenticationException;

public class PasswordExpiredException extends AuthenticationException implements CustomCredentialException {
    public PasswordExpiredException() {
        super(ResponseCode.OUT_OF_DATE_PASSWORD.getMessage());
    }

    @Override
    public String getErrorCode() {
        return ResponseCode.OUT_OF_DATE_PASSWORD.getCode();
    }
}
