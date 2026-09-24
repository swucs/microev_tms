package com.obigo.microev.tms.admin.service;

import com.obigo.microev.tms.admin.presentation.center.*;
import com.obigo.microev.tms.admin.presentation.center.GetVehicleDriverResDto;

import java.util.List;

public interface CenterService {
    List<SearchCentersResDto> searchCenters(SearchCentersReqDto reqDto);

    long createCenter(CreateCenterReqDto reqDto);

    CenterDetailResDto getCenterDetail(Long centerSeq);

    void modifyCenter(Long centerSeq, ModifyCenterReqDto reqDto);

    void removeCenter(Long centerSeq);

    List<GetVehicleDriverResDto> getVehiclesByCenterSeq(Long centerSeq);
}
