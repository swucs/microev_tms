package com.obigo.microev.tms.api.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obigo.microev.tms.api.infrastructure.jwt.JwtTokenProvider;
import com.obigo.microev.tms.api.infrastructure.threadLocal.CurrentUser;
import com.obigo.microev.tms.api.infrastructure.threadLocal.CurrentUserContextHolder;
import com.obigo.microev.tms.api.presentation.ResponseDto;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import com.obigo.microev.tms.core.domain.mapper.DriverMapper;
import com.obigo.microev.tms.core.exception.BusinessException;
import com.obigo.microev.tms.core.exception.UnauthorizedException;
import com.obigo.microev.tms.lib.redis.entity.DriverToken;
import com.obigo.microev.tms.lib.redis.repository.DriverTokenRedisRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Optional;



@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final DriverTokenRedisRepository driverTokenRedisRepository;
    private final DriverMapper driverMapper;


    /**
     * 요청이 들어올 때마다 실행되는 메소드
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            //1. 토큰이 유효한지 체크
            String accessToken = jwtTokenProvider.resolveToken(request);
            if (accessToken == null) {
                throw new BusinessException(ResponseCode.INVALID_TOKEN);
            }

            log.debug("URI : {}", request.getRequestURI());
            log.debug("accessToken : {}", accessToken);


            //2. 토큰정보가 redis에 있는지 체크
            Optional<DriverToken> userTokenOptional = driverTokenRedisRepository.findById(accessToken);

            //3. 토큰의 userId가 redis에 없으면 로그인 오류
            if (userTokenOptional.isEmpty()) {
                throw new UnauthorizedException();
            }

            //4. 토큰이 유효하면 해당 User정보를 ThreadLocal에 저장
            CurrentUserContextHolder.set(new CurrentUser(userTokenOptional.get()));


        } catch (ExpiredJwtException e) {
            log.info("ExpiredJwtException : {}", e.getMessage());
            this.setErrorResponse(HttpStatus.UNAUTHORIZED, response, ResponseCode.EXPIRED_TOKEN);
            return false;
        } catch (UnauthorizedException e) {
            log.info("UnauthorizedException : {}", e.getMessage());
            this.setErrorResponse(HttpStatus.UNAUTHORIZED, response, e.getResponseCode());
            return false;
        } catch (BusinessException e) {
            log.info("BusinessException : {}", e.getMessage());
            this.setErrorResponse(HttpStatus.UNAUTHORIZED, response, e.getResponseCode());
            return false;
        } catch (Exception e) {
            log.info("Invalid Token : {}", e.getMessage());
            this.setErrorResponse(HttpStatus.UNAUTHORIZED, response, ResponseCode.INVALID_TOKEN);
            return false;
        }

        return true;
    }


    /**
     * Controller 요청이 끝날 때마다 실행되는 메소드
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //ThreadLocal에 저장된 User정보를 제거
        CurrentUserContextHolder.clear();
    }

    /**
     * 토큰이 유효한지 체크 후 DB에서 조회 후 Redis에 저장 후 ThreadLocal에 저장
     * @param loginId
     * @param accessToken
     */
    /**private void checkTokenFromDatabase(String loginId, String accessToken) {
        Optional<Driver> driverOptional = driverMapper.findByLoginId(loginId);

        //4. 토큰의 userId가 DB에 없으면 예외처리
        if (driverOptional.isEmpty()) {
            throw new BusinessException(ResponseCode.INVALID_ACCOUNT_ID);
        }

        Driver driver = driverOptional.get();
        DriverToken driverToken = DriverToken.builder()
                .accessToken(accessToken)
                .driverSeq(driver.getDriverSeq())
                .loginId(driver.getLoginId())
                .email(driver.getEmail())
                .build();
        driverTokenRedisRepository.save(driverToken);

        //5. 토큰이 유효하면 해당 User정보를 ThreadLocal에 저장
        CurrentUserContextHolder.set(new CurrentUser(driverToken));
    }*/



    private void setErrorResponse(HttpStatus httpStatus, HttpServletResponse response, ResponseCode errorCode) {
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        try {
            response.getWriter().write(objectMapper.writeValueAsString(new ResponseDto<>(errorCode.getCode(), errorCode.getMessage(),null)));
        } catch (Exception e) {
            log.error("Error : {}", e.getMessage());
        }
    }
}
