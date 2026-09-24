package com.obigo.microev.tms.admin.service.impl;

import com.obigo.microev.tms.admin.converter.CenterConverter;
import com.obigo.microev.tms.admin.presentation.center.*;
import com.obigo.microev.tms.admin.presentation.center.GetVehicleDriverResDto;
import com.obigo.microev.tms.admin.service.CenterService;
import com.obigo.microev.tms.admin.utils.AuthenticationUtils;
import com.obigo.microev.tms.admin.vo.CurrentAdmin;
import com.obigo.microev.tms.core.domain.entity.Center;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import com.obigo.microev.tms.core.domain.mapper.CenterMapper;
import com.obigo.microev.tms.core.domain.mapper.VehicleMapper;
import com.obigo.microev.tms.core.domain.mapper.vo.center.CenterDetailResult;
import com.obigo.microev.tms.core.domain.mapper.vo.center.SearchCentersCondition;
import com.obigo.microev.tms.core.domain.mapper.vo.vehicle.BelongingVehicleResult;
import com.obigo.microev.tms.core.exception.InvalidRequestException;
import com.obigo.microev.tms.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CenterServiceImpl implements CenterService {
    private final CenterConverter centerConverter;
    private final CenterMapper centerMapper;
    private final VehicleMapper vehicleMapper;

    /**
     * 센터 검색 조회
     * @param reqDto
     * @return
     */
    @Override
    public List<SearchCentersResDto> searchCenters(SearchCentersReqDto reqDto) {
        SearchCentersCondition condition = centerConverter.toSearchCentersCondition(reqDto);
        return centerMapper.findByConditions(condition).stream()
                .map(centerConverter::toSearchCentersResDto)
                .toList();
    }

    /**
     * 센터 생성
     * @param reqDto
     * @return
     */
    @Override
    public long createCenter(CreateCenterReqDto reqDto) {
        Center center = centerConverter.toCenter(reqDto);

        // insert 센터
        centerMapper.insert(center);
        
        return center.getCenterSeq();
    }

    /**
     * 센터 상세 조회
     * @param centerSeq
     * @return
     */
    @Override
    public CenterDetailResDto getCenterDetail(Long centerSeq) {
        CenterDetailResult centerDetailResult = centerMapper.findDetailById(centerSeq);

        if (centerDetailResult == null) {
            throw new NotFoundException(ResponseCode.NOT_FOUND_CENTER);
        }


        List<BelongingVehicleResult> vehicles = vehicleMapper.findByCenterSeq(centerSeq);

        CenterDetailResDto centerDetailResDto = centerConverter.toCenterDetailResDto(centerDetailResult);
        centerDetailResDto.setBelongingVehicles(centerConverter.toVehicles(vehicles));

        return centerDetailResDto;
    }


    /**
     * 센터 수정
     *
     * @param centerSeq
     * @param reqDto
     */
    @Override
    public void modifyCenter(Long centerSeq, ModifyCenterReqDto reqDto) {
        Center center = centerMapper.findById(centerSeq);
        if (center == null) {
            throw new NotFoundException(ResponseCode.NOT_FOUND_CENTER);
        }

        CurrentAdmin currentAdmin = AuthenticationUtils.getCurrentAdmin();

        // update 센터
        assert currentAdmin != null;
        centerConverter.updateCenter(center, reqDto);
        centerMapper.update(center);
    }


    /**
     * 센터 삭제
     * @param centerSeq
     */
    @Override
    public void removeCenter(Long centerSeq) {
        Center center = centerMapper.findById(centerSeq);
        if (center == null) {
            throw new NotFoundException(ResponseCode.NOT_FOUND_CENTER);
        }

        //해당 센터의 차량이 존재하는지 체크
        vehicleMapper.findByCenterSeq(centerSeq).stream()
                .findFirst()
                .ifPresent(vehicleByCenterSeqResult -> {
                    throw new InvalidRequestException(ResponseCode.EXIST_VEHICLE_IN_REGION);
                });


        //delete 센터
        centerMapper.deleteById(centerSeq);
    }

    /**
     * 해당 센터의 차량정보를 조회한다.
     * @param centerSeq
     * @return
     */
    @Override
    public List<GetVehicleDriverResDto> getVehiclesByCenterSeq(Long centerSeq) {
        List<BelongingVehicleResult> vehicles = vehicleMapper.findByCenterSeq(centerSeq);
        return centerConverter.toGetVehicleDriverResDto(vehicles);
    }
}
