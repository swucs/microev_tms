package com.obigo.microev.tms.admin.service.impl;

import com.obigo.microev.tms.admin.converter.VehicleConverter;
import com.obigo.microev.tms.admin.presentation.vehicle.CreateVehicleReqDto;
import com.obigo.microev.tms.admin.presentation.vehicle.ModifyVehicleReqDto;
import com.obigo.microev.tms.admin.presentation.vehicle.SearchVehiclesReqDto;
import com.obigo.microev.tms.admin.presentation.vehicle.SearchVehiclesResDto;
import com.obigo.microev.tms.admin.service.VehicleService;
import com.obigo.microev.tms.core.domain.entity.Driver;
import com.obigo.microev.tms.core.domain.entity.Vehicle;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import com.obigo.microev.tms.core.domain.mapper.DeliveryMapper;
import com.obigo.microev.tms.core.domain.mapper.DriverMapper;
import com.obigo.microev.tms.core.domain.mapper.VehicleMapper;
import com.obigo.microev.tms.core.domain.mapper.vo.vehicle.SearchVehiclesCondition;
import com.obigo.microev.tms.core.domain.mapper.vo.vehicle.SearchVehiclesResult;
import com.obigo.microev.tms.core.exception.InvalidRequestException;
import com.obigo.microev.tms.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VehicleServiceImpl implements VehicleService {
    private final VehicleConverter vehicleConverter;
    private final VehicleMapper vehicleMapper;
    private final DriverMapper driverMapper;
    private final DeliveryMapper deliveryMapper;


    /**
     * 차량 목록 검색 및 조회
     * @param reqDto
     * @return
     */
    @Override
    public List<SearchVehiclesResDto> searchVehicles(SearchVehiclesReqDto reqDto) {
        SearchVehiclesCondition condition = vehicleConverter.toSearchVehiclesCondition(reqDto);
        List<SearchVehiclesResult> searchVehiclesResults = vehicleMapper.findByConditions(condition);
        return vehicleConverter.toSearchVehiclesResDtos(searchVehiclesResults);
    }

    /**
     * 차량 등록
     * @param createVehicleReqDto
     * @return
     */
    @Override
    public Long createVehicle(CreateVehicleReqDto createVehicleReqDto) {
        //차량번호 중복 체크
        vehicleMapper.findByVehicleNum(createVehicleReqDto.getVehicleNum())
                .ifPresent(vehicle -> {
                    throw new InvalidRequestException(ResponseCode.DUPLICATED_VEHICLE_NUM);
                });
        
        //차량등록번호 중복 체크
        vehicleMapper.findByVehicleRegNum(createVehicleReqDto.getVehicleRegNum())
                .ifPresent(vehicle -> {
                    throw new InvalidRequestException(ResponseCode.DUPLICATED_VEHICLE_REG_NUM);
                });

        //차량에 이미 등록된 기사인지 체크
        if (createVehicleReqDto.getDriverSeq() != null) {
            vehicleMapper.findByDriverSeq(createVehicleReqDto.getDriverSeq())
                    .ifPresent(vehicle -> {
                        throw new InvalidRequestException(ResponseCode.DUPLICATED_VEHICLE_DRIVER);
                    });

            //해당 기사의 센터가 일치하는지 체크
            Driver driver = driverMapper.findById(createVehicleReqDto.getDriverSeq())
                    .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_DRIVER));
            if (!driver.getCenterSeq().equals(createVehicleReqDto.getCenterSeq())) {
                throw new InvalidRequestException(ResponseCode.NOT_MATCHED_DRIVER_CENTER);
            }
        }

        Vehicle vehicle = vehicleConverter.toVehicle(createVehicleReqDto);
        vehicleMapper.insert(vehicle);
        return vehicle.getVehicleSeq();
    }

    /**
     * 차량 수정
     * @param modifyVehicleReqDto
     * @return
     */
    @Override
    public void modifyVehicle(Long vehicleSeq, ModifyVehicleReqDto modifyVehicleReqDto) {
        Vehicle vehicle = vehicleMapper.findById(vehicleSeq)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_VEHICLE));

        //차량번호 중복 체크
        vehicleMapper.findByVehicleNum(modifyVehicleReqDto.getVehicleNum())
                .ifPresent(v -> {
                    if (!v.getVehicleSeq().equals(vehicleSeq)) {
                        throw new InvalidRequestException(ResponseCode.DUPLICATED_VEHICLE_NUM);
                    }
                });

        //차량등록번호 중복 체크
        vehicleMapper.findByVehicleRegNum(modifyVehicleReqDto.getVehicleRegNum())
                .ifPresent(v -> {
                    if (!v.getVehicleSeq().equals(vehicleSeq)) {
                        throw new InvalidRequestException(ResponseCode.DUPLICATED_VEHICLE_REG_NUM);
                    }
                });


        //차량에 이미 등록된 기사인지 체크
        if (modifyVehicleReqDto.getDriverSeq() != null) {
            vehicleMapper.findByDriverSeq(modifyVehicleReqDto.getDriverSeq())
                    .ifPresent(v -> {
                        if (!v.getVehicleSeq().equals(vehicleSeq)) {
                            throw new InvalidRequestException(ResponseCode.DUPLICATED_VEHICLE_DRIVER);
                        }
                    });

            //기사가 변경된 경우 진행중인 배차에 해당된 차량인지 체크
            if (!Objects.equals(vehicle.getDriverSeq(), modifyVehicleReqDto.getDriverSeq())) {
                int processingCount = deliveryMapper.findCountProcessingDispatch(vehicleSeq);
                if (processingCount > 0) {
                    throw new InvalidRequestException(ResponseCode.CANNOT_CHANGE_DRIVER_PROCESSING_DISPATCH);
                }
            }


            //해당 기사의 센터가 일치하는지 체크
            Driver driver = driverMapper.findById(modifyVehicleReqDto.getDriverSeq())
                    .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_DRIVER));
            if (!driver.getCenterSeq().equals(modifyVehicleReqDto.getCenterSeq())) {
                throw new InvalidRequestException(ResponseCode.NOT_MATCHED_DRIVER_CENTER);
            }
        }

        //Update Vehicle
        vehicleConverter.updateVehicle(vehicle, modifyVehicleReqDto);
        vehicleMapper.update(vehicle);
    }


    /**
     * 차량 삭제
     * @param vehicleSeq
     */
    @Override
    public void deleteVehicle(Long vehicleSeq) {
        vehicleMapper.findById(vehicleSeq)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_VEHICLE));

        vehicleMapper.deleteById(vehicleSeq);
    }

}
