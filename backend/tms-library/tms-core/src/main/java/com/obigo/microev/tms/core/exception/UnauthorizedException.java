package com.obigo.microev.tms.core.exception;


import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import lombok.Getter;

/**
 * 인증정보가 없어서 발생하는= 예외
 * ExceptionHandler에서 401 Unauthorized로 응답처리한다.
 * 500 에러를 발생시킬 때는 BusinessException를 사용한다.
 */
@Getter
public class UnauthorizedException extends AbstractException {
    public UnauthorizedException() {
        super(ResponseCode.REQUIRED_LOGIN.getMessage());
        this.responseCode = ResponseCode.REQUIRED_LOGIN;
    }
}
