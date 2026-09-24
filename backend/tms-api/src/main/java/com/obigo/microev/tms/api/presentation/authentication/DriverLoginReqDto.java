package com.obigo.microev.tms.api.presentation.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DriverLoginReqDto {

    @NotBlank(message = "{validation.api.login.loginId.notBlank}")
    @Schema(description = "Login ID", example = "driver03")
    private String loginId;

    @NotBlank(message = "{validation.api.login.password.notBlank}")
    @Schema(description = "PIN 숫자4자리", example = "1234")
    private String password;
}
