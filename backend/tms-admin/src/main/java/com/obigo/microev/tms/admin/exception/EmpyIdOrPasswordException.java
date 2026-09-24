package com.obigo.microev.tms.admin.exception;


import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import org.springframework.security.core.AuthenticationException;

public class EmpyIdOrPasswordException extends AuthenticationException implements CustomCredentialException {
    public EmpyIdOrPasswordException() {
        super(ResponseCode.EMPTY_ID_OR_PASSSWORD.getMessage());
    }

    @Override
    public String getErrorCode() {
        return ResponseCode.EMPTY_ID_OR_PASSSWORD.getCode();
    }
}
