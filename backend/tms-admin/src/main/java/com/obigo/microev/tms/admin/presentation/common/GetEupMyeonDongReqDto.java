package com.obigo.microev.tms.admin.presentation.common;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class GetEupMyeonDongReqDto {

    @Schema(description = "검색어")
    @NotBlank(message = "{validation.common.coordinate.address.notBlank}")
    @Size(min = 2, message = "{validation.common.coordinate.address.size}")
    private String address;
}
