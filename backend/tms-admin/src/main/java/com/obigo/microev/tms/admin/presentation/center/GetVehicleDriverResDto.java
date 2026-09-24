package com.obigo.microev.tms.admin.presentation.center;

import lombok.Data;

@Data
public class GetVehicleDriverResDto {
    private Long vehicleSeq;
    private String vehicleName;
    private Long driverSeq;
    private String driverName;
    private String vehicleNum;
}
