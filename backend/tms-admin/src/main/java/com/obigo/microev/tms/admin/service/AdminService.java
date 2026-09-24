package com.obigo.microev.tms.admin.service;

import com.obigo.microev.tms.admin.presentation.admin.CreateAdminReqDto;
import com.obigo.microev.tms.admin.presentation.admin.ModifyAdminReqDto;
import com.obigo.microev.tms.admin.presentation.admin.SearchAdminsReqDto;
import com.obigo.microev.tms.admin.presentation.admin.SearchAdminsResDto;

import java.util.List;

public interface AdminService {
    List<SearchAdminsResDto> searchAdmins(SearchAdminsReqDto reqDto);

    long createAdmin(CreateAdminReqDto reqDto);

    void modifyAdmin(Long adminSeq, ModifyAdminReqDto reqDto);
}
