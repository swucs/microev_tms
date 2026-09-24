package com.obigo.microev.tms.admin.presentation.region;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

@Data
public class SearchRegionsReqDto {
    
    @Parameter(description = "권역명")
    private String regionName;

    @Parameter(description = "차량명")
    private String vehicleName;

    @Parameter(description = "읍면동")
    private String eupMyeonDong;
}
