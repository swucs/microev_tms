package com.obigo.microev.tms.admin.converter;

import com.obigo.microev.tms.admin.presentation.center.*;
import com.obigo.microev.tms.admin.presentation.center.GetVehicleDriverResDto;
import com.obigo.microev.tms.admin.utils.AuthenticationUtils;
import com.obigo.microev.tms.core.domain.entity.Center;
import com.obigo.microev.tms.core.domain.mapper.vo.center.CenterDetailResult;
import com.obigo.microev.tms.core.domain.mapper.vo.center.SearchCentersCondition;
import com.obigo.microev.tms.core.domain.mapper.vo.center.SearchCentersResult;
import com.obigo.microev.tms.core.domain.mapper.vo.vehicle.BelongingVehicleResult;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE
        , imports = {LocalDateTime.class, AuthenticationUtils.class}
)
public interface CenterConverter {

    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "creatorSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    Center toCenter(CreateCenterReqDto createCenterReqDto);


    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    void updateCenter(@MappingTarget Center center, ModifyCenterReqDto modifyCenterReqDto);

    SearchCentersCondition toSearchCentersCondition(SearchCentersReqDto reqDto);

    SearchCentersResDto toSearchCentersResDto(SearchCentersResult searchCentersResult);

    CenterDetailResDto toCenterDetailResDto(CenterDetailResult centerDetailResult);

    List<CenterDetailResDto.Vehicle> toVehicles(List<BelongingVehicleResult> vehicles);

    List<GetVehicleDriverResDto> toGetVehicleDriverResDto(List<BelongingVehicleResult> vehicles);
}
