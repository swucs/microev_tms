package com.obigo.microev.tms.admin.service;

import com.obigo.microev.tms.admin.presentation.vehicle.CreateVehicleReqDto;
import com.obigo.microev.tms.admin.presentation.vehicle.ModifyVehicleReqDto;
import com.obigo.microev.tms.admin.presentation.vehicle.SearchVehiclesReqDto;
import com.obigo.microev.tms.admin.presentation.vehicle.SearchVehiclesResDto;

import java.util.List;

public interface VehicleService {
    List<SearchVehiclesResDto> searchVehicles(SearchVehiclesReqDto reqDto);

    Long createVehicle(CreateVehicleReqDto createVehicleReqDto);

    void modifyVehicle(Long vehicleSeq, ModifyVehicleReqDto modifyVehicleReqDto);

    void deleteVehicle(Long vehicleSeq);
}
