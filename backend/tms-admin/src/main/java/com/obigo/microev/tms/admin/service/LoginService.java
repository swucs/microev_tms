package com.obigo.microev.tms.admin.service;

import com.obigo.microev.tms.admin.presentation.login.LoginResDto;
import com.obigo.microev.tms.admin.presentation.login.ReissueTokenReqDto;
import com.obigo.microev.tms.admin.presentation.login.ReissueTokenResDto;
import com.obigo.microev.tms.core.domain.entity.Admin;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LoginService {
    int MAX_FAIL_COUNT = 5;

    Optional<Admin> getAdminByEmail(String email);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    int processFailedLogin(Admin admin, String ipAddress);

    @Transactional
    LoginResDto processSuccessfulLogin(Long adminSeq, String ipAddress);

    @Transactional
    ReissueTokenResDto reissueToken(ReissueTokenReqDto reqDto);
}
