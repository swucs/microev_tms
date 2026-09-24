package com.obigo.microev.tms.admin.presentation.login;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReissueTokenReqDto {

    @NotBlank(message = "{validation.login.refreshToken.notBlank}")
    private String refreshToken;

    @NotBlank(message = "{validation.login.email.notBlank}")
    private String email;
}
