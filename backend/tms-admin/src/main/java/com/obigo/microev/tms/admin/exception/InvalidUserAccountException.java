package com.obigo.microev.tms.admin.exception;


import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import org.springframework.security.core.AuthenticationException;

public class InvalidUserAccountException extends AuthenticationException implements CustomCredentialException {
    public InvalidUserAccountException() {
        super(ResponseCode.INVALID_ACCOUNT_ID.getMessage());
    }

    @Override
    public String getErrorCode() {
        return ResponseCode.INVALID_ACCOUNT_ID.getCode();
    }
}
