package com.obigo.microev.tms.admin.converter;

import com.obigo.microev.tms.admin.presentation.vehicle.CreateVehicleReqDto;
import com.obigo.microev.tms.admin.presentation.vehicle.ModifyVehicleReqDto;
import com.obigo.microev.tms.admin.presentation.vehicle.SearchVehiclesReqDto;
import com.obigo.microev.tms.admin.presentation.vehicle.SearchVehiclesResDto;
import com.obigo.microev.tms.admin.utils.AuthenticationUtils;
import com.obigo.microev.tms.core.domain.entity.Vehicle;
import com.obigo.microev.tms.core.domain.mapper.vo.vehicle.SearchVehiclesCondition;
import com.obigo.microev.tms.core.domain.mapper.vo.vehicle.SearchVehiclesResult;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE
        , imports = {LocalDateTime.class, AuthenticationUtils.class}
)
public interface VehicleConverter {

    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "creatorSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    Vehicle toVehicle(CreateVehicleReqDto createVehicleReqDto);

    SearchVehiclesCondition toSearchVehiclesCondition(SearchVehiclesReqDto reqDto);

    List<SearchVehiclesResDto> toSearchVehiclesResDtos(List<SearchVehiclesResult> searchVehiclesResults);

    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    void updateVehicle(@MappingTarget Vehicle vehicle, ModifyVehicleReqDto modifyVehicleReqDto);
}
