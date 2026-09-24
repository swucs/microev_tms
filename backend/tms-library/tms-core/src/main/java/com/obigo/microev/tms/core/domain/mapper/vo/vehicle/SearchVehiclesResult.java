package com.obigo.microev.tms.core.domain.mapper.vo.vehicle;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchVehiclesResult {
    private Long vehicleSeq;
    private Long centerSeq;
    private String centerName;
    private Long regionSeq;
    private String regionName;
    private Long driverSeq;
    private String driverName;
    private String vehicleName;
    private String model;
    private String vehicleTypeCd;
    private String vehicleTypeCdName;
    private String vehicleNum;
    private String vehicleRegNum;
    private String modelYear;
    private String vehicleRegArea;
    private String ownerName;
    private String length;
    private String width;
    private String tonGrade;
    private String maxLoadingCapacity;
    private String fuelTypeCd;
    private String fuelTypeCdName;
    private String fuelEfficiency;
    private String vehicleUseTypeCd;
    private String vehicleUseTypeCdName;
    private String garageName;
    private String usageYn;
    private Long creatorSeq;
    private String creatorName;
    private LocalDateTime createdAt;
}
