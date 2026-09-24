package com.obigo.microev.tms.admin.service.impl;


import com.obigo.microev.tms.admin.converter.DriverConverter;
import com.obigo.microev.tms.admin.presentation.driver.CreateDriverReqDto;
import com.obigo.microev.tms.admin.presentation.driver.ModifyDriverPasswordReqDto;
import com.obigo.microev.tms.admin.presentation.driver.ModifyDriverReqDto;
import com.obigo.microev.tms.admin.presentation.driver.SearchDriversReqDto;
import com.obigo.microev.tms.admin.presentation.driver.SearchDriversResDto;
import com.obigo.microev.tms.admin.service.DriverService;
import com.obigo.microev.tms.core.domain.entity.Driver;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import com.obigo.microev.tms.core.domain.mapper.DriverMapper;
import com.obigo.microev.tms.core.domain.mapper.VehicleMapper;
import com.obigo.microev.tms.core.domain.mapper.vo.driver.SearchDriversCondition;
import com.obigo.microev.tms.core.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DriverServiceImpl implements DriverService {
    private final DriverConverter driverConverter;
    private final DriverMapper driverMapper;
    private final VehicleMapper vehicleMapper;
    private final PasswordEncoder passwordEncoder;


    /**
     * 기사관리 목록 검색
     * @param reqDto
     * @return
     */
    @Override
    public List<SearchDriversResDto> searchDrivers(SearchDriversReqDto reqDto) {
        SearchDriversCondition condition = driverConverter.toSearchDriverCondition(reqDto);
        return driverMapper.findByConditions(condition).stream()
                .map(driverConverter::toSearchDriversResDto)
                .toList();
    }


    /**
     * 기사 등록
     * @param reqDto
     * @return
     */
    @Override
    public long createDriver(CreateDriverReqDto reqDto) {
        //Login ID 중복체크
        driverMapper.findByLoginId(reqDto.getLoginId())
                .ifPresent(driver -> {
                    throw new InvalidRequestException(ResponseCode.DUPLICATED_DRIVER_LOGIN_ID);
                });


        //외부시스템연동ID 중복체크
        driverMapper.findByExtSystemLinkedId(reqDto.getExtSystemLinkedId())
                .ifPresent(driver -> {
                    throw new InvalidRequestException(ResponseCode.DUPLICATED_DRIVER_EXT_SYSTEM_LINKED_ID);
                });


        Driver driver = driverConverter.toDriver(reqDto);
        
        //비밀번호 암호화
        driver.setPassword(passwordEncoder.encode(driver.getPassword()));

        driverMapper.insert(driver);
        return driver.getDriverSeq();
    }

    /**
     * 기사 수정
     * @param reqDto
     */
    @Override
    public void modifyDriver(Long driverSeq, ModifyDriverReqDto reqDto) {
        Driver driver = driverMapper.findById(driverSeq)
                .orElseThrow(() -> new InvalidRequestException(ResponseCode.NOT_FOUND_DRIVER));

        //외부시스템연동ID 중복체크
        driverMapper.findByExtSystemLinkedId(reqDto.getExtSystemLinkedId())
                .ifPresent(d -> {
                    if(!d.getDriverSeq().equals(driverSeq)) {
                        throw new InvalidRequestException(ResponseCode.DUPLICATED_DRIVER_EXT_SYSTEM_LINKED_ID);
                    }
                });

        //해당 기사가 차량에 소속되면 센터 수정 불가
        if (!driver.getCenterSeq().equals(reqDto.getCenterSeq())) {
            vehicleMapper.findByDriverSeq(driverSeq)
                    .ifPresent(vehicle -> {
                        throw new InvalidRequestException(ResponseCode.NOT_ALLOWED_UPDATE_CENTER_EXIST_VEHICLE);
                    });
        }

        driverConverter.updateDriver(driver, reqDto);

        driverMapper.update(driver);
    }

    /**
     * 기사 비밀번호 수정
     * @param driverSeq
     * @param reqDto
     */
    @Override
    public void modifyDriverPassword(Long driverSeq, ModifyDriverPasswordReqDto reqDto) {
        Driver driver = driverMapper.findById(driverSeq)
                .orElseThrow(() -> new InvalidRequestException(ResponseCode.NOT_FOUND_DRIVER));

        driverConverter.updateDriver(driver, reqDto);

        //비밀번호 암호화
        driver.setPassword(passwordEncoder.encode(driver.getPassword()));

        driverMapper.update(driver);
    }


    /**
     * 기사 삭제
     * @param driverSeq
     */
    @Override
    public void removeDriver(Long driverSeq) {
        //기사 존재여부 체크
        driverMapper.findById(driverSeq)
                .orElseThrow(() -> new InvalidRequestException(ResponseCode.NOT_FOUND_DRIVER));

        //기사가 소속된 차량이 존재하면 삭제 불가
        vehicleMapper.findByDriverSeq(driverSeq)
                .ifPresent(vehicle -> {
                    throw new InvalidRequestException(ResponseCode.EXIST_VEHICLE_IN_DRIVER);
                });

        driverMapper.deleteById(driverSeq);
    }
}
