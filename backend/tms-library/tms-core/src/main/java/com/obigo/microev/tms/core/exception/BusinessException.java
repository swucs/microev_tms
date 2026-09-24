package com.obigo.microev.tms.core.exception;


import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import lombok.Getter;


/**
 * 로직을 처리하던 중 예외상황이 발생하는 경우 예외
 * ExceptionHandler에서 500 server error로 응답처리한다.
 * 400 에러를 발생시킬 때는 InvalidRequestException를 사용한다.
 */
@Getter
public class BusinessException extends AbstractException {
    public BusinessException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }


    /**
     * 메시지에 파라미터를 포함하는 경우
     * @param responseCode
     * @param args
     */
    public BusinessException(ResponseCode responseCode, Object... args) {
        super(String.format(responseCode.getMessage(), args));
        this.responseCode = responseCode;
    }
}
