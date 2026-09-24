package com.obigo.microev.tms.core.domain.mapper.vo.vehicle;

import lombok.Data;

@Data
public class VehicleByEupMyeonDongResult {

    private Long vehicleSeq;
    private String vehicleName;
    private Long regionSeq;
    private String regionName;
    private String driverName;
}
