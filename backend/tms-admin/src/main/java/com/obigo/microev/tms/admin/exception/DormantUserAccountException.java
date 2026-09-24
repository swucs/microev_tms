package com.obigo.microev.tms.admin.exception;


import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class DormantUserAccountException extends AuthenticationException implements CustomCredentialException {
    public DormantUserAccountException() {
        super(ResponseCode.DORMANT_ADMIN_ID.getMessage());
    }

    @Override
    public String getErrorCode() {
        return ResponseCode.DORMANT_ADMIN_ID.getCode();
    }
}
