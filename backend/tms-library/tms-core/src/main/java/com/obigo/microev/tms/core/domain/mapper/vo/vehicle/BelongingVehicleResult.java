package com.obigo.microev.tms.core.domain.mapper.vo.vehicle;

import lombok.Data;

@Data
public class BelongingVehicleResult {

    private Long vehicleSeq;
    private String vehicleName;
    private String vehicleNum;
    private Long regionSeq;
    private Long centerSeq;
    private String length;
    private String width;
    private String maxLoadingCapacity;
    private Long driverSeq;
    private String driverName;
}
