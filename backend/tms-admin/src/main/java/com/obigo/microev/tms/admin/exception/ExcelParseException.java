package com.obigo.microev.tms.admin.exception;


import com.obigo.microev.tms.admin.excel.ExcelError;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import com.obigo.microev.tms.core.exception.AbstractException;
import lombok.Getter;

import java.util.List;

/**
 * 엑셀 업로드 중 데이터 파싱 과정에서 발생하는 예외
 * ExceptionHandler에서 400 bad request로 응답처리한다.
 */
@Getter
public class ExcelParseException extends AbstractException {
    private final List<ExcelError> errors;

    public ExcelParseException(List<ExcelError> errors) {
        super(ResponseCode.EXCEL_PARSE_FAILED.getMessage());
        super.responseCode = ResponseCode.EXCEL_PARSE_FAILED;
        this.errors = errors;
    }
}
