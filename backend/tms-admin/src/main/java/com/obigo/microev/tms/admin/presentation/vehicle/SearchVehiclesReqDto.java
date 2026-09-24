package com.obigo.microev.tms.admin.presentation.vehicle;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

@Data
public class SearchVehiclesReqDto {
    @Parameter(description = "차량명")
    private String vehicleName;

    @Parameter(description = "센터명")
    private String centerName;

    @Parameter(description = "차량유형")
    private String vehicleTypeCd;
}
