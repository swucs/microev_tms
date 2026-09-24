package com.obigo.microev.tms.api.service.impl;

import com.obigo.microev.tms.api.infrastructure.jwt.JwtTokenProvider;
import com.obigo.microev.tms.api.infrastructure.threadLocal.CurrentUser;
import com.obigo.microev.tms.api.infrastructure.threadLocal.CurrentUserContextHolder;
import com.obigo.microev.tms.api.presentation.authentication.DriverLoginReqDto;
import com.obigo.microev.tms.api.presentation.authentication.DriverLoginResDto;
import com.obigo.microev.tms.api.service.AuthenticationService;
import com.obigo.microev.tms.core.domain.entity.Driver;
import com.obigo.microev.tms.core.domain.entity.Vehicle;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import com.obigo.microev.tms.core.domain.mapper.DriverMapper;
import com.obigo.microev.tms.core.domain.mapper.VehicleMapper;
import com.obigo.microev.tms.core.exception.InvalidRequestException;
import com.obigo.microev.tms.lib.redis.entity.DriverToken;
import com.obigo.microev.tms.lib.redis.repository.DriverTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtTokenProvider jwtTokenProvider;
    private final DriverMapper driverMapper;
    private final VehicleMapper vehicleMapper;
    private final DriverTokenRedisRepository driverTokenRedisRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 배송기사 로그인 처리
     * @param reqDto
     * @return
     */
    @Override
    public DriverLoginResDto login(DriverLoginReqDto reqDto) {

        //10. DB에서 차량사용자 정보 조회
        Driver driver = driverMapper.findByLoginId(reqDto.getLoginId())
                .orElseThrow(() -> new InvalidRequestException(ResponseCode.INVALID_ACCOUNT_ID));

        //20. 암호화 된 PIN 비교
        if (!passwordEncoder.matches(reqDto.getPassword(), driver.getPassword())) {
            throw new InvalidRequestException(ResponseCode.WRONG_PASSWORD);
        }

        //30. JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(driver.getLoginId());
        String refreshToken = jwtTokenProvider.createRefreshToken(driver.getLoginId());

        //31. 차량 정보 조회
        Vehicle vehicle = vehicleMapper.findByDriverSeq(driver.getDriverSeq())
                .orElseGet(() -> Vehicle.builder().build());


        //40. 응답 DTO 생성
        DriverLoginResDto driverLoginResDto = DriverLoginResDto.builder()
                .driverSeq(driver.getDriverSeq())
                .driverName(driver.getDriverName())
                .vehicleSeq(vehicle.getVehicleSeq())
                .vehicleName(vehicle.getVehicleName())
                .loginId(driver.getLoginId())
                .email(driver.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        //50. Redis에 토큰 저장
        DriverToken driverToken = DriverToken.builder()
                .accessToken(accessToken)
                .driverSeq(driver.getDriverSeq())
                .loginId(driver.getLoginId())
                .email(driver.getEmail())
                .build();
        driverTokenRedisRepository.save(driverToken);

        return driverLoginResDto;
    }


    /**
     * 로그아웃 처리
     */
    @Override
    public void logout() {
        CurrentUser currentUser = CurrentUserContextHolder.get();

        //Redis에서 토큰 삭제
        driverTokenRedisRepository.deleteById(currentUser.getAccessToken());
    }


    /**
     * 모바일용 토큰 발급
     * @return
     */
    @Override
    public DriverLoginResDto getTokenForMobile() {
        CurrentUser currentUser = CurrentUserContextHolder.get();

        //JWT 토큰 생성
        String loginId = currentUser.getLoginId();
        String accessToken = jwtTokenProvider.createAccessToken(loginId);
        String refreshToken = jwtTokenProvider.createRefreshToken(loginId);

        //기사정보 조회
        Driver driver = driverMapper.findByLoginId(loginId)
                .orElseThrow(() -> new InvalidRequestException(ResponseCode.INVALID_ACCOUNT_ID));

        //차량 정보 조회
        Vehicle vehicle = vehicleMapper.findByDriverSeq(currentUser.getDriverSeq())
                .orElseGet(() -> Vehicle.builder().build());

        //Redis에 토큰 저장
        DriverToken driverToken = DriverToken.builder()
                .accessToken(accessToken)
                .driverSeq(driver.getDriverSeq())
                .loginId(driver.getLoginId())
                .email(driver.getEmail())
                .build();
        driverTokenRedisRepository.save(driverToken);


        //응답 DTO 생성
        return DriverLoginResDto.builder()
                .driverSeq(driver.getDriverSeq())
                .driverName(driver.getDriverName())
                .vehicleSeq(vehicle.getVehicleSeq())
                .vehicleName(vehicle.getVehicleName())
                .loginId(driver.getLoginId())
                .email(driver.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
