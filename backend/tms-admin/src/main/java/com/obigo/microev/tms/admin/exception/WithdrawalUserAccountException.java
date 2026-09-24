package com.obigo.microev.tms.admin.exception;


import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import org.springframework.security.core.AuthenticationException;

public class WithdrawalUserAccountException extends AuthenticationException implements CustomCredentialException {
    public WithdrawalUserAccountException() {
        super(ResponseCode.DISABLED_ADMIN_ID.getMessage());
    }

    @Override
    public String getErrorCode() {
        return ResponseCode.DISABLED_ADMIN_ID.getCode();
    }
}
