package com.obigo.microev.tms.admin.converter;

import com.obigo.microev.tms.admin.presentation.driver.CreateDriverReqDto;
import com.obigo.microev.tms.admin.presentation.driver.ModifyDriverPasswordReqDto;
import com.obigo.microev.tms.admin.presentation.driver.ModifyDriverReqDto;
import com.obigo.microev.tms.admin.presentation.driver.SearchDriversReqDto;
import com.obigo.microev.tms.admin.presentation.driver.SearchDriversResDto;
import com.obigo.microev.tms.admin.utils.AuthenticationUtils;
import com.obigo.microev.tms.core.domain.mapper.vo.driver.SearchDriversCondition;
import com.obigo.microev.tms.core.domain.mapper.vo.driver.SearchDriversResult;
import com.obigo.microev.tms.core.domain.entity.Driver;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE
        , imports = {LocalDateTime.class, AuthenticationUtils.class}
)
public interface DriverConverter {

    SearchDriversResDto toSearchDriversResDto(SearchDriversResult result);

    SearchDriversCondition toSearchDriverCondition(SearchDriversReqDto searchDriversReqDto);

    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "creatorSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    Driver toDriver(CreateDriverReqDto createDriverReqDto);


    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    void updateDriver(@MappingTarget Driver driver, ModifyDriverReqDto modifyDriverReqDto);

    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    void updateDriver(@MappingTarget Driver driver, ModifyDriverPasswordReqDto reqDto);
}
