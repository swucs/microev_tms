package com.obigo.microev.tms.core.exception;


import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import lombok.Getter;

/**
 * 요청한 정보가 없을때
 * ExceptionHandler에서 404 Not Found 로 응답처리한다.
 */
@Getter
public class NotFoundException extends AbstractException {
    public NotFoundException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }
}
