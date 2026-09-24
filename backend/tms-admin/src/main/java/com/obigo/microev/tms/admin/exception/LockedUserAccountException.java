package com.obigo.microev.tms.admin.exception;


import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class LockedUserAccountException extends AuthenticationException implements CustomCredentialException {
    public LockedUserAccountException() {
        super(ResponseCode.LOCKED_ADMIN_ID.getMessage());
    }

    @Override
    public String getErrorCode() {
        return ResponseCode.LOCKED_ADMIN_ID.getCode();
    }
}
