package com.obigo.microev.tms.admin.converter;

import com.obigo.microev.tms.admin.presentation.commonCode.*;
import com.obigo.microev.tms.admin.utils.AuthenticationUtils;
import com.obigo.microev.tms.core.domain.entity.CommonCode;
import com.obigo.microev.tms.core.domain.entity.CommonCodeGroup;
import com.obigo.microev.tms.core.domain.mapper.vo.commonCode.SearchCommonCodeGroupsCondition;
import com.obigo.microev.tms.core.domain.mapper.vo.commonCode.SearchCommonCodeGroupsResult;
import com.obigo.microev.tms.core.domain.mapper.vo.commonCode.ValidCommonCodesResult;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE
        , imports = {LocalDateTime.class, AuthenticationUtils.class}
)
public interface CommonCodeConverter {

    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "creatorSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    CommonCodeGroup toCommonCodeGroup(CreateCommonCodeGroupReqDto reqDto);

    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    void updateCommonCodeGroup(@MappingTarget CommonCodeGroup commonCodeGroup, ModifyCommonCodeGroupReqDto reqDto);


    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "creatorSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    CommonCode toCommonCode(CreateCommonCodeReqDto reqDto);

    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    void updateCommonCode(@MappingTarget CommonCode commonCode, ModifyCommonCodeReqDto reqDto);

    List<GetValidCommonCodesResDto> toGetValidCommonCodesResDto(List<ValidCommonCodesResult> validCodes);

    SearchCommonCodeGroupsCondition toSearchCommonCodeGroupsCondition(SearchCommonCodeGroupsReqDto reqDto);

    List<SearchCommonCodeGroupsResDto> toSearchCommonCodeGroupsResDto(List<SearchCommonCodeGroupsResult> searchCommonCodeGroupsResults);

    List<GetCommonCodesResDto> toGetCommonCodesResDto(List<CommonCode> commonCodes);


}
