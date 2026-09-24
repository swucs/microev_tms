package com.obigo.microev.tms.api.presentation.delivery;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GetWaybillsReqDto {
    @NotBlank(message = "{validation.api.delivery.address.notBlank}")
    @Parameter(description = "도로명주소", example = "서울시 강남구 테헤란로 123")
    private String address;
}
