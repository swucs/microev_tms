package com.obigo.microev.tms.admin.config.security.jwt;


import com.obigo.microev.tms.admin.config.SecurityConfig;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import com.obigo.microev.tms.core.exception.AbstractException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.obigo.microev.tms.admin.utils.ErrorResponseUtil.setErrorResponse;


/**
 * JwtFilter : request 앞단에 붙이는 필터. http request에서 토큰을 받아와 정상 토큰일 경우 security context에 저장
 * OncePerRequestFilter는 모든 서블릿에 일관된 요청을 처리하기 위해 만들어진 필터
 * 사용자의 한 번의 요청 당 딱 한 번만 실행되는 필터이다.
 */

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private static final PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        request = new ReReadableRequestWrapper(request);

        String token = jwtTokenProvider.resolveToken(request);
        String requestURI = request.getRequestURI();

        if (!isTokenRequiresUri(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            // Access 토큰이 만료되었는지 검사
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (AbstractException e) {
            setErrorResponse(response, e.getResponseCode());
        } catch (ExpiredJwtException e) {
            setErrorResponse(response, ResponseCode.EXPIRED_TOKEN);
        } catch (Exception e) {
            log.info("Exception", e);
            setErrorResponse(response, ResponseCode.INVALID_TOKEN);
        }
    }


    private boolean isTokenRequiresUri(String requestURI) {
        //  요청받은 Uri가 Token을 확인해야하는 Uri인지 여부 확인
        List<String> permitAllUrls = Arrays.asList(SecurityConfig.PERMIT_URLS);
        for (String pattern : permitAllUrls) {
            if (pathMatcher.match(pattern, requestURI)) {
                return false; // Permit URLs에 해당되므로 토큰 검사가 필요하지 않음
            }
        }
        return true; // Permit URLs에 해당되지 않으므로 토큰 검사가 필요함
    }
}