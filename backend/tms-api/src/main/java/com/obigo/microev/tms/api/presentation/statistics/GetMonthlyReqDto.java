package com.obigo.microev.tms.api.presentation.statistics;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GetMonthlyReqDto {
    @NotBlank(message = "{validation.api.statistics.yyyyMM.notBlank}")
    @Parameter(description = "년월")
    private String yyyyMM;


}
