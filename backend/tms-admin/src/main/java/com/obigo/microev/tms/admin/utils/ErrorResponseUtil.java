package com.obigo.microev.tms.admin.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obigo.microev.tms.admin.presentation.ResponseDto;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * filter 에서 터진 오류를 잡기 위해 사용
 */
public class ErrorResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static void setErrorResponse(
            HttpServletResponse response,
            ResponseCode errorCode
    ) {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        try {
            response.getWriter().write(objectMapper.writeValueAsString(new ResponseDto<>(errorCode.getCode(), errorCode.getMessage(),null)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}