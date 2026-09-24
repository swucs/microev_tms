package com.obigo.microev.tms.api.service;

import com.obigo.microev.tms.api.presentation.authentication.DriverLoginReqDto;
import com.obigo.microev.tms.api.presentation.authentication.DriverLoginResDto;

public interface AuthenticationService {
    DriverLoginResDto login(DriverLoginReqDto reqDto);

    void logout();

    DriverLoginResDto getTokenForMobile();
}
