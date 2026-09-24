package com.obigo.microev.tms.admin.presentation.common;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GetCoordinateReqDto {

    @Schema(description = "주소", example = "서울시 강남구 역삼동")
    @NotBlank(message = "{validation.common.coordinate.address.notBlank}")
    @Size(min = 2, message = "{validation.common.coordinate.address.size}")
    private String address;
}
