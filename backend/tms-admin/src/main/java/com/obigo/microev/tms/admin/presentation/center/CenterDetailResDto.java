package com.obigo.microev.tms.admin.presentation.center;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CenterDetailResDto {
    private Long centerSeq;
    private String centerName;
    private String centerTypeCd;
    private String centerTypeCdName;
    private String zipCode;
    private String centerAddr1;
    private String centerAddr2;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal totalArea;
    private String managerName;
    private String contactNum;
    private String usageYn;
    private Integer vehicleCount;
    private List<Vehicle> belongingVehicles;
    private String creatorName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Data
    public static class Vehicle {
        private Long vehicleSeq;
        private String vehicleName;
        private String vehicleNum;
        private String length;
        private String width;
        private String maxLoadingCapacity;
        private String driverName;
    }
}
