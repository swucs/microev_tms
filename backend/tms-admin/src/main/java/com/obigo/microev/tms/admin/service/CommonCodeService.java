package com.obigo.microev.tms.admin.service;

import com.obigo.microev.tms.admin.presentation.commonCode.*;

import java.util.List;

public interface CommonCodeService {
    List<GetValidCommonCodesResDto> getValidCommonCodes(String comCodeGroupCd);

    List<SearchCommonCodeGroupsResDto> searchCommonCodeGroups(SearchCommonCodeGroupsReqDto reqDto);

    CreateCommonCodeGroupResDto createCommonCodeGroup(CreateCommonCodeGroupReqDto reqDto);

    ModifyCommonCodeGroupResDto modifyCommonCodeGroup(ModifyCommonCodeGroupReqDto reqDto);

    void deleteCommonCodeGroups(DeleteCommonCodeGroupReqDto reqDto);

    List<GetCommonCodesResDto> getCommonCodes(String comCodeGroupCd);

    Long createCommonCodes(CreateCommonCodeReqDto reqDto);

    void modifyCommonCodes(Long comCodeSeq, ModifyCommonCodeReqDto reqDto);

    void deleteCommonCodes(DeleteCommonCodeReqDto reqDto);
}
