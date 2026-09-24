package com.obigo.microev.tms.admin.exception;


import com.obigo.microev.tms.admin.excel.ExcelError;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import com.obigo.microev.tms.core.exception.AbstractException;
import lombok.Getter;

import java.util.List;

/**
 * 엑셀 업로드하여 데이터를 저장하는 과정에서 발생하는 예외
 * ExceptionHandler에서 400 bad request로 응답처리한다.
 */
@Getter
public class ExcelUploadException extends AbstractException {
    private final List<ExcelError> errors;

    public ExcelUploadException(List<ExcelError> errors) {
        super(ResponseCode.EXCEL_UPLOAD_FAILED.getMessage());
        super.responseCode = ResponseCode.EXCEL_UPLOAD_FAILED;
        this.errors = errors;
    }
}
