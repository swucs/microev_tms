package com.obigo.microev.tms.core.exception;

import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import lombok.Getter;

@Getter
public abstract class AbstractException extends RuntimeException {
    protected ResponseCode responseCode;

    public AbstractException() {
        super();
    }

    public AbstractException(String message) {
        super(message);
    }
}
