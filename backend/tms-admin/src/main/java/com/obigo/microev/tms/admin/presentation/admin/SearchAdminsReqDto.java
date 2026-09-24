package com.obigo.microev.tms.admin.presentation.admin;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

@Data
public class SearchAdminsReqDto {

    @Parameter(description = "Email(ID)")
    private String email;

    @Parameter(description = "관리자명")
    private String adminName;

    @Parameter(description = "상태")
    private String statusCd;
}
