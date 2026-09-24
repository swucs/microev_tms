package com.obigo.microev.tms.admin.service;

import com.obigo.microev.tms.admin.presentation.region.*;

import java.util.List;

public interface RegionService {
    List<SearchRegionsResDto> searchRegions(SearchRegionsReqDto reqDto);

    long createRegion(CreateRegionReqDto reqDto);

    RegionDetailResDto getRegionDetail(Long regionSeq);

    void modifyRegion(Long regionSeq, ModifyRegionReqDto reqDto);

    void removeRegion(Long regionSeq);
}
