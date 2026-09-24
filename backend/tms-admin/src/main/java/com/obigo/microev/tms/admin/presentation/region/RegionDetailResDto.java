package com.obigo.microev.tms.admin.presentation.region;

import lombok.Data;

import java.util.List;

@Data
public class RegionDetailResDto {
    private Long regionSeq;
    private String regionName;
    private List<String> eupMyeonDongs;
    private List<Vehicle> belongingVehicles;

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
