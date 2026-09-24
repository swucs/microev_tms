package com.obigo.microev.tms.core.domain.entity;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Vehicle {
    private Long vehicleSeq;
    private Long centerSeq;
    private Long regionSeq;
    private Long driverSeq;
    private String vehicleName;
    private String model;
    private String vehicleTypeCd;
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
    private String fuelEfficiency;
    private String vehicleUseTypeCd;
    private String garageName;
    private String usageYn;
    private LocalDateTime createdAt;
    private Long creatorSeq;
    private LocalDateTime modifiedAt;
    private Long modifierSeq;
}
