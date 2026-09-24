package com.obigo.microev.tms.admin.service.impl;

import com.obigo.microev.tms.admin.config.security.jwt.JwtTokenProvider;
import com.obigo.microev.tms.admin.presentation.login.LoginResDto;
import com.obigo.microev.tms.admin.presentation.login.ReissueTokenReqDto;
import com.obigo.microev.tms.admin.presentation.login.ReissueTokenResDto;
import com.obigo.microev.tms.admin.service.LoginService;
import com.obigo.microev.tms.core.domain.entity.Admin;
import com.obigo.microev.tms.core.domain.enumeration.AdminStatus;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import com.obigo.microev.tms.core.domain.mapper.AdminMapper;
import com.obigo.microev.tms.core.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * PortalUser 조회
     * @param email
     * @return
     */
    @Override
    public Optional<Admin> getAdminByEmail(String email) {
        return adminMapper.findByEmail(email);
    }



    /**
     * 로그인 실패시 처리
     * @param admin
     * @param ipAddress
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public int processFailedLogin(Admin admin, String ipAddress) {
        LocalDateTime now = LocalDateTime.now();

        Long adminSeq = admin.getAdminSeq();


        //@로그인 마지막 로그인 정보 업데이트
        //로그인 실패 횟수 증가 및 마지막접속일시 변경
        int failCount = admin.addFailCount();

        //로그인 실패가 최대 횟수를 초과한 경우
        if (admin.getFailCount() >= this.MAX_FAIL_COUNT) {
            //상태 변경(잠김상태)
            admin.changeStatus(AdminStatus.Locked);
        }
        adminMapper.updateAccessInfo(admin);

        return failCount;
    }


    /**
     * 로그인 성공 처리
     * @param adminSeq
     * @param ipAddress
     */
    @Transactional
    @Override
    public LoginResDto processSuccessfulLogin(Long adminSeq, String ipAddress) {

        Admin admin = adminMapper.findById(adminSeq).orElseThrow();
        LocalDateTime now = LocalDateTime.now();

        //토큰 생성
        String email = admin.getEmail();
        String newAccessToken = jwtTokenProvider.createAccessToken(email);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(email);


        //@로그인 마지막 로그인 정보 업데이트
        //로그인 실패 횟수 초기화
        admin.resetFailCount();

        //마지막 성공 접속시간 Update
        admin.changeLastAccessedAt(newRefreshToken);
        adminMapper.updateAccessInfo(admin);

        return LoginResDto.of(admin, newAccessToken, newRefreshToken);
    }

    /**
     * access-token, refresh-token 재발급
     */
    @Override
    @Transactional
    public ReissueTokenResDto reissueToken(ReissueTokenReqDto reqDto) {
        String refreshToken = reqDto.getRefreshToken();

        // refresh-token JWT 검증
        String email = jwtTokenProvider.parseToken(refreshToken);

        // Email이 일치하는지 체크
        if (!StringUtils.equals(reqDto.getEmail(), email)) {
            throw new InvalidRequestException(ResponseCode.INVALID_REFRESH_TOKEN);
        }

        Admin admin = adminMapper.findByEmail(email)
                .orElseThrow(() -> new InvalidRequestException(ResponseCode.INVALID_REFRESH_TOKEN));

        // DB에 저장된 Refresh Token과 일치하지 않으면 다시 로그인 하도록 유도
        if (!StringUtils.equals(admin.getRefreshToken(), refreshToken)) {
            throw new InvalidRequestException(ResponseCode.INVALID_REFRESH_TOKEN);
        }

        //access-token 재발급
        String newAccessToken = jwtTokenProvider.createAccessToken(email);

        //refresh-token 재발급
        String newRefreshToken = jwtTokenProvider.createRefreshToken(email);

        //DB에 refresh-token 저장
        admin.changeRefreshToken(newRefreshToken);
        adminMapper.updateRefreshToken(admin);

        return ReissueTokenResDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
