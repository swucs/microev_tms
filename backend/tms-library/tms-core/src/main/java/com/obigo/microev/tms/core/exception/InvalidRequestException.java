package com.obigo.microev.tms.core.exception;


import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import lombok.Getter;

/**
 * 사용자 요청이 잘못된 경우 발생하는 예외
 * ExceptionHandler에서 400 bad request로 응답처리한다.
 * 500 에러를 발생시킬 때는 BusinessException를 사용한다.
 */
@Getter
public class InvalidRequestException extends AbstractException {
    public InvalidRequestException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }
}
