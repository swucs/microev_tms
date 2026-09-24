package com.obigo.microev.tms.admin.presentation;

import com.obigo.microev.tms.admin.excel.ExcelError;
import com.obigo.microev.tms.admin.exception.ExcelParseException;
import com.obigo.microev.tms.admin.exception.ExcelUploadException;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import com.obigo.microev.tms.core.exception.BusinessException;
import com.obigo.microev.tms.core.exception.InvalidRequestException;
import com.obigo.microev.tms.core.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestControllerAdvice()
public class ExceptionHandlers {

    /**
     *  요청 값이 올바르지 않는 경우 예외 처리
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<Void>> handle(MethodArgumentNotValidException ex) {

        StringBuffer message = new StringBuffer();
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        AtomicInteger index = new AtomicInteger(1);
        allErrors.forEach((error) -> {
            log.info("MethodArgumentNotValidException : {}", error.getDefaultMessage());
            message.append(error.getDefaultMessage());
            if (index.get() < allErrors.size()) {
                message.append("\n");
            }
            index.getAndIncrement();
        });

        ResponseDto<Void> responseDto = ResponseDto.<Void>builder()
                .resultCode(ResponseCode.INVALID_REQUEST_INFO.getCode())
                .resultMessage(message.toString())
                .build();

        return ResponseEntity.badRequest().body(responseDto);
    }


    /**
     * 요청 값이 올바르지 않는 경우 예외 처리 : validation에서 error 발생하면 HandlerMethodValidationException 발생됨
     * @param ex
     * @return
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ResponseDto<Void>> handle(HandlerMethodValidationException ex) {

        StringBuffer message = new StringBuffer();
        List<? extends MessageSourceResolvable> allErrors = ex.getAllErrors();
        AtomicInteger index = new AtomicInteger(1);
        allErrors.forEach((error) -> {
            log.info("HandlerMethodValidationException : {}", error.getDefaultMessage());
            message.append(error.getDefaultMessage());
            if (index.get() < allErrors.size()) {
                message.append("\n");
            }
            index.getAndIncrement();
        });

        ResponseDto<Void> responseDto = ResponseDto.<Void>builder()
                .resultCode(ResponseCode.INVALID_REQUEST_INFO.getCode())
                .resultMessage(message.toString())
                .build();

        return ResponseEntity.badRequest().body(responseDto);
    }


    /**
     * HttpMessageNotReadableException 요청 JSON 데이터가 올바르지 않은 경우
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDto<Void>> handle(HttpMessageNotReadableException ex) {

        log.info("HttpMessageNotReadableException : {}", ex.getMessage());

        ResponseCode responseCode = ResponseCode.INVALID_REQUEST_INFO;
        ResponseDto<Void> responseDto = ResponseDto.<Void>builder()
                .resultCode(responseCode.getCode())
                .resultMessage(responseCode.getMessage())
                .build();

        return ResponseEntity.badRequest().body(responseDto);
    }


    /**
     * MethodArgumentTypeMismatchException 요청 값의 데이터 타입이 올바르지 않는 경우
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseDto<Void>> handle(MethodArgumentTypeMismatchException ex) {

        log.info("MethodArgumentTypeMismatchException : {}", ex.getMessage());

        ResponseCode responseCode = ResponseCode.TYPE_MISMATCH_REQUEST_INFO;
        ResponseDto<Void> responseDto = ResponseDto.<Void>builder()
                .resultCode(responseCode.getCode())
                .resultMessage(responseCode.getMessage())
                .build();

        return ResponseEntity.badRequest().body(responseDto);
    }

    /**
     * InvalidRequestException 요청값이 올바르지 않는 처리
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ResponseDto<Void>> handle(InvalidRequestException ex) {

        log.info("InvalidRequestException : {}", ex.getMessage());

        ResponseDto<Void> responseDto = ResponseDto.<Void>builder()
                .resultCode(ex.getResponseCode().getCode())
                .resultMessage(ex.getResponseCode().getMessage())
                .build();

        return ResponseEntity.badRequest().body(responseDto);
    }

    /**
     * BusinessException 처리
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseDto<Void>> handle(BusinessException ex) {

        log.info("BusinessException : {}", ex.getMessage());

        ResponseDto<Void> responseDto = ResponseDto.<Void>builder()
                .resultCode(ex.getResponseCode().getCode())
                .resultMessage(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().body(responseDto);
    }

    /**
     * NotFoundException 처리 -> 404
     * @param ex
     * @return
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDto<Void>> handle(NotFoundException ex) {
        log.info("404 NotFoundException : {}", ex.getMessage());

        ResponseCode responseCode = ResponseCode.NOT_FOUND_RESOURCE;
        ResponseDto<Void> responseDto = ResponseDto.<Void>builder()
                .resultCode(responseCode.getCode())
                .resultMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
    }


    /**
     * NoResourceFoundException 처리 -> 404
     * @param ex
     * @return
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ResponseDto<Void>> handle(NoResourceFoundException ex) {
        log.info("404 NoResourceFoundException : {}", ex.getMessage());

        ResponseCode responseCode = ResponseCode.NOT_FOUND_RESOURCE;
        ResponseDto<Void> responseDto = ResponseDto.<Void>builder()
                .resultCode(responseCode.getCode())
                .resultMessage(responseCode.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
    }


    /**
     * Request Header 누락 처리
     * @param ex
     * @return
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ResponseDto<Void>> handle(MissingRequestHeaderException ex) {

        log.info("MissingRequestHeaderException : {}", ex.getMessage());

        ResponseCode responseCode = ResponseCode.MISSING_REQUIRED_VALUE;
        ResponseDto<Void> responseDto = ResponseDto.<Void>builder()
                .resultCode(responseCode.getCode())
                .resultMessage(responseCode.getMessage() + " : " + ex.getMessage())
                .build();

        return ResponseEntity.badRequest().body(responseDto);
    }

    /**
     * ExcelParseException 처리
     * @param ex
     * @return
     */
    @ExceptionHandler(ExcelParseException.class)
    public ResponseEntity<ResponseDto<List<ExcelError>>> handle(ExcelParseException ex) {

        log.info("ExcelParseException : {}", ex.getMessage());

        ResponseDto<List<ExcelError>> responseDto = ResponseDto.<List<ExcelError>>builder()
                .resultCode(ex.getResponseCode().getCode())
                .resultMessage(ex.getResponseCode().getMessage())
                .data(ex.getErrors())
                .build();

        return ResponseEntity.badRequest().body(responseDto);
    }


    /**
     * ExcelParseException 처리
     * @param ex
     * @return
     */
    @ExceptionHandler(ExcelUploadException.class)
    public ResponseEntity<ResponseDto<List<ExcelError>>> handle(ExcelUploadException ex) {

        log.info("ExcelUploadException : {}", ex.getMessage());

        ResponseDto<List<ExcelError>> responseDto = ResponseDto.<List<ExcelError>>builder()
                .resultCode(ex.getResponseCode().getCode())
                .resultMessage(ex.getResponseCode().getMessage())
                .data(ex.getErrors())
                .build();

        return ResponseEntity.badRequest().body(responseDto);
    }



    /**
     * Exception 처리
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Void>> handle(Exception ex) {

        log.error("Exception", ex);

        ResponseCode responseCode = ResponseCode.SERVER_EXCEPTION;

        ResponseDto<Void> responseDto = ResponseDto.<Void>builder()
                .resultCode(responseCode.getCode())
                .resultMessage(responseCode.getMessage())
                .build();

        return ResponseEntity.internalServerError().body(responseDto);
    }
}
