package com.obigo.microev.tms.admin.presentation.login;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReissueTokenResDto {
    private String accessToken;
    private String refreshToken;
}
