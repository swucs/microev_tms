package com.obigo.microev.tms.admin.converter;


import com.obigo.microev.tms.admin.presentation.admin.CreateAdminReqDto;
import com.obigo.microev.tms.admin.presentation.admin.ModifyAdminReqDto;
import com.obigo.microev.tms.admin.presentation.admin.SearchAdminsReqDto;
import com.obigo.microev.tms.admin.presentation.admin.SearchAdminsResDto;
import com.obigo.microev.tms.admin.utils.AuthenticationUtils;
import com.obigo.microev.tms.core.domain.entity.Admin;
import com.obigo.microev.tms.core.domain.enumeration.AdminStatus;
import com.obigo.microev.tms.core.domain.mapper.vo.admin.SearchAdminsCondition;
import com.obigo.microev.tms.core.domain.mapper.vo.admin.SearchAdminsResult;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE
        , imports = {LocalDateTime.class, AuthenticationUtils.class, AdminStatus.class}
)
public interface AdminConverter {

    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "creatorSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    @Mapping(target = "failCount", constant = "0")
    @Mapping(target = "statusCd", expression = "java(AdminStatus.Normal.name())")
    Admin toAdmin(CreateAdminReqDto createAdminReqDto);

    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    void updateAdmin(@MappingTarget Admin admin, ModifyAdminReqDto modifyAdminReqDto);

    SearchAdminsCondition toSearchAdminsCondition(SearchAdminsReqDto reqDto);

    List<SearchAdminsResDto> toSearchAdminsResDtos(List<SearchAdminsResult> searchAdminsResults);
}
