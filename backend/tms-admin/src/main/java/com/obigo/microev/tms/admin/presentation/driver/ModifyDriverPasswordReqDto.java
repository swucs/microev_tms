package com.obigo.microev.tms.admin.presentation.driver;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ModifyDriverPasswordReqDto {
    @Schema(description = "비밀번호")
    @NotBlank(message = "{validation.driver.password.notBlank}")
    private String password;
}
