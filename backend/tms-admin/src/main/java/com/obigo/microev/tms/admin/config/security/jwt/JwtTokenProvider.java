package com.obigo.microev.tms.admin.config.security.jwt;


import com.obigo.microev.tms.admin.config.security.AdminDetails;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import com.obigo.microev.tms.core.exception.BusinessException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * 유저 정보로 jwt access/refresh 토큰 생성 및 재발급 + 토큰으로부터 유저 정보 받아옴
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.token.access-expiration-time}")
    private long accessExpirationTime;

    @Value("${jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;

    private final AdminDetailsServiceImpl userDetailsService;


    public String createAccessToken(String email) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessExpirationTime); // 토큰 유효시간 설정

        // 1. secretKey를 바이트 배열로 변환 (signWith 메서드 기존의 방식을 deprecated함)
        byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        // 2. 바이트 배열로 SecretKeySpec 객체 생성
        Key key = new SecretKeySpec(secretKeyBytes, 0, secretKeyBytes.length, "HmacSHA256");

        return Jwts.builder()
                .setClaims(Jwts.claims().setSubject(email)) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(expireDate) // set Expire Time
                .signWith(key, SignatureAlgorithm.HS256) // 사용할 암호화 알고리즘과 signature 에 들어갈 secret값 세팅
                .compact(); // 직렬화하여 문자열로 변환
    }

    /**
     * Refresh Token 생성 메서드
     */
    public String createRefreshToken(String email) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshExpirationTime); // 토큰 유효시간 설정


        byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        Key key = new SecretKeySpec(secretKeyBytes, 0, secretKeyBytes.length, "HmacSHA256");


        return Jwts.builder()
                .setClaims(Jwts.claims().setSubject(email)) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(expireDate) // set Expire Time
                .signWith(key, SignatureAlgorithm.HS256) // 사용할 암호화 알고리즘과 signature 에 들어갈 secret값 세팅
                .compact(); // 직렬화하여 문자열로 변환
    }


    /*
     * 토큰으로부터 클레임을 만들고, 이를 통해 User 객체 생성하여 Authentication 객체 반환
     */
    public Authentication getAuthentication(String token) {
        try {
            String email = this.parseToken(token);

            AdminDetails userDetails = (AdminDetails) userDetailsService.loadUserByUsername(email);
            return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());

        } catch (Exception e) {
            throw new BusinessException(ResponseCode.EXPIRED_TOKEN);
        }
    }


    /**
     * http 헤더로부터 bearer 토큰을 가져옴
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization"); // Authorization 헤더에서 토큰 정보를 가져옴
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) { // 토큰이 null이 아니고, Bearer로 시작하는 경우
            return bearerToken.substring(7); // "Bearer " 이후의 문자열을 반환
        }
        return null;
    }


    // 토큰에서 이메일을 추출하는 메서드
    public String parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
