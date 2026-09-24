package com.obigo.microev.tms.admin.presentation.driver;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

@Data
public class SearchDriversReqDto {
    
    @Parameter(description = "기사명")
    private String driverName;

    @Parameter(description = "센터명")
    private String centerName;

    @Parameter(description = "상태")
    private String statusCd;
}
