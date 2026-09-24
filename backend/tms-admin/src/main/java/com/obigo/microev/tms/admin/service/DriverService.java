package com.obigo.microev.tms.admin.service;

import com.obigo.microev.tms.admin.presentation.driver.CreateDriverReqDto;
import com.obigo.microev.tms.admin.presentation.driver.ModifyDriverPasswordReqDto;
import com.obigo.microev.tms.admin.presentation.driver.ModifyDriverReqDto;
import com.obigo.microev.tms.admin.presentation.driver.SearchDriversReqDto;
import com.obigo.microev.tms.admin.presentation.driver.SearchDriversResDto;

import java.util.List;

public interface DriverService {
    List<SearchDriversResDto> searchDrivers(SearchDriversReqDto reqDto);

    long createDriver(CreateDriverReqDto reqDto);

    void modifyDriver(Long driverSeq, ModifyDriverReqDto reqDto);

    void modifyDriverPassword(Long driverSeq, ModifyDriverPasswordReqDto reqDto);

    void removeDriver(Long driverSeq);
}
