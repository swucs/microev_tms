package com.obigo.microev.tms.admin.presentation.dispatch;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

@Data
public class SearchDispatchesReqDto {

    @Parameter(description = "센터명")
    private String centerName;

    @Parameter(description = "배차상태")
    private String dispatchStatusCd;
}
