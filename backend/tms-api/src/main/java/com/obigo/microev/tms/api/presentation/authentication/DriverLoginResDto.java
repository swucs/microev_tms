package com.obigo.microev.tms.api.presentation.authentication;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DriverLoginResDto {
    private Long driverSeq;
    private String driverName;
    private Long vehicleSeq;
    private String vehicleName;
    private String loginId;
    private String email;
    private String accessToken;
    private String refreshToken;
}
