package com.obigo.microev.tms.admin.service;

import com.obigo.microev.tms.admin.presentation.common.GetCoordinateReqDto;
import com.obigo.microev.tms.admin.presentation.common.GetCoordinateResDto;
import com.obigo.microev.tms.admin.presentation.common.GetEupMyeonDongReqDto;
import com.obigo.microev.tms.admin.presentation.common.GetEupMyeonDongResDto;

public interface CommonService {
    GetCoordinateResDto getCoordinate(GetCoordinateReqDto reqDto) throws Exception;

    GetEupMyeonDongResDto getEupMyeonDong(GetEupMyeonDongReqDto reqDto) throws Exception;
}
