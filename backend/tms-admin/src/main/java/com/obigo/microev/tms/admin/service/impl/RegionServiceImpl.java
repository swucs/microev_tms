package com.obigo.microev.tms.admin.service.impl;

import com.obigo.microev.tms.admin.converter.RegionConverter;
import com.obigo.microev.tms.admin.presentation.region.*;
import com.obigo.microev.tms.admin.service.RegionService;
import com.obigo.microev.tms.admin.utils.AuthenticationUtils;
import com.obigo.microev.tms.admin.vo.CurrentAdmin;
import com.obigo.microev.tms.core.domain.entity.Region;
import com.obigo.microev.tms.core.domain.entity.RegionRange;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import com.obigo.microev.tms.core.domain.mapper.RegionMapper;
import com.obigo.microev.tms.core.domain.mapper.VehicleMapper;
import com.obigo.microev.tms.core.domain.mapper.vo.region.SearchRegionsCondition;
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
public class RegionServiceImpl implements RegionService {
    private final RegionConverter regionConverter;
    private final RegionMapper regionMapper;
    private final VehicleMapper vehicleMapper;

    /**
     * 권역 검색 조회
     * @param reqDto
     * @return
     */
    @Override
    public List<SearchRegionsResDto> searchRegions(SearchRegionsReqDto reqDto) {
        SearchRegionsCondition condition = regionConverter.toSearchRegionsCondition(reqDto);
        return regionMapper.findByConditions(condition).stream()
                .map(regionConverter::toSearchRegionsResDto)
                .toList();
    }

    /**
     * 권역 생성
     * @param reqDto
     * @return
     */
    @Override
    public long createRegion(CreateRegionReqDto reqDto) {

        Region region = regionConverter.toRegion(reqDto);

        // insert 권역
        regionMapper.insert(region);

        // insert 권역범위
        regionConverter.toRegionRanges(reqDto, region.getRegionSeq())
                .forEach(regionRange -> {
                    RegionRange regionRangeById = regionMapper.findRegionRangeById(regionRange);
                    if (regionRangeById != null) {
                        throw new InvalidRequestException(ResponseCode.DUPLICATED_EUP_MYUN_DONG);
                    }
                    regionMapper.insertRegionRange(regionRange);
                });

        return region.getRegionSeq();
    }

    /**
     * 권역 상세 조회
     * @param regionSeq
     * @return
     */
    @Override
    public RegionDetailResDto getRegionDetail(Long regionSeq) {

        Region region = regionMapper.findById(regionSeq);
        List<RegionRange> regionRanges = regionMapper.findRegionRangesByRegionSeq(regionSeq);
        List<BelongingVehicleResult> vehicles = vehicleMapper.findByRegionSeq(regionSeq);

        RegionDetailResDto regionDetailResDto = regionConverter.toRegionDetailResDto(region);
        regionDetailResDto.setEupMyeonDongs(regionRanges.stream().map(RegionRange::getEupMyeonDong).toList());
        regionDetailResDto.setBelongingVehicles(regionConverter.toVehicles(vehicles));

        return regionDetailResDto;
    }


    /**
     * 권역 수정
     *
     * @param regionSeq
     * @param reqDto
     */
    @Override
    public void modifyRegion(Long regionSeq, ModifyRegionReqDto reqDto) {
        Region region = regionMapper.findById(regionSeq);
        if (region == null) {
            throw new NotFoundException(ResponseCode.NOT_FOUND_REGION);
        }

        CurrentAdmin currentAdmin = AuthenticationUtils.getCurrentAdmin();

        // update 권역
        assert currentAdmin != null;
        regionConverter.updateRegion(region, reqDto);
        regionMapper.update(region);

        // delete 권역범위
        regionMapper.deleteRegionRangeByRegionId(region.getRegionSeq());

        // insert 권역범위
        regionConverter.toRegionRanges(reqDto, region.getRegionSeq())
                .forEach(regionRange -> {
                    RegionRange regionRangeById = regionMapper.findRegionRangeById(regionRange);
                    if (regionRangeById != null) {
                        throw new InvalidRequestException(ResponseCode.DUPLICATED_EUP_MYUN_DONG);
                    }
                    regionMapper.insertRegionRange(regionRange);
                });
    }


    /**
     * 권역 삭제
     * @param regionSeq
     */
    @Override
    public void removeRegion(Long regionSeq) {
        Region region = regionMapper.findById(regionSeq);
        if (region == null) {
            throw new NotFoundException(ResponseCode.NOT_FOUND_REGION);
        }

        //해당 권역의 차량이 존재하는지 체크
        vehicleMapper.findByRegionSeq(regionSeq).stream()
                .findFirst()
                .ifPresent(belongingVehicleResult -> {
                    throw new InvalidRequestException(ResponseCode.EXIST_VEHICLE_IN_REGION);
                });

        //delete 권역범위
        regionMapper.deleteRegionRangeByRegionId(regionSeq);

        //delete 권역
        regionMapper.delete(regionSeq);

    }
}
