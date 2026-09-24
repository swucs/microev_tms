package com.obigo.microev.tms.api.aspect;


import com.obigo.microev.tms.api.infrastructure.threadLocal.CurrentUser;
import com.obigo.microev.tms.api.infrastructure.threadLocal.CurrentUserContextHolder;
import com.obigo.microev.tms.api.service.ApiCallService;
import com.obigo.microev.tms.core.domain.entity.ApiCall;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ApiCallAspect {

    private final ApiCallService apiCallService;


    @Pointcut("within(com.obigo.microev.tms.api.presentation..*)")
    public void onRequest() {}


    @Around("onRequest()")
    public Object  around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("API 호출 전처리");

        ApiCall apiCall = null;
        try {
            CurrentUser currentUser = CurrentUserContextHolder.get();
            Long driverSeq = currentUser == null ? null : currentUser.getDriverSeq();
            LocalDateTime now = LocalDateTime.now();

            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String httpMethod = request.getMethod();
            String requestUrl = request.getRequestURI();

            //API Call 정보
            apiCall = ApiCall.builder()
                    .apiUrl(requestUrl)
                    .httpMethod(httpMethod)
                    .startTime(now)
                    .driverSeq(driverSeq)
                    .build();
        } catch (Exception e) {
            log.error("API Call 정보 생성 오류", e);
            throw new RuntimeException(e);
        }

        //실제 로직 수행
        Object result = joinPoint.proceed();


        //API Call 결과
        try {
            apiCall.setEndTime(LocalDateTime.now());

            log.debug("API Call 결과 : {}", result);

            if (result instanceof ResponseEntity<?> responseEntity) {
                apiCall.setResponseCode(String.valueOf(responseEntity.getStatusCode().value()));
            } else if (result instanceof SseEmitter) {
                apiCall.setResponseCode(String.valueOf(HttpStatus.OK.value()));
            }

            log.info("API apiCall {}", apiCall);
            apiCallService.createApiCall(apiCall);
        } catch (Exception e) {
            log.error("API Call 결과 저장 오류", e);
            throw new RuntimeException(e);
        }

        return result;
    }
}
