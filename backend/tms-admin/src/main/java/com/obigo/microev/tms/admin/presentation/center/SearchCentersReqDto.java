package com.obigo.microev.tms.admin.presentation.center;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

@Data
public class SearchCentersReqDto {

    @Parameter(description = "센터명")
    private String centerName;

    @Parameter(description = "센터유형코드")
    private String centerTypeCd;

    @Parameter(description = "담당자")
    private String managerName;
}
