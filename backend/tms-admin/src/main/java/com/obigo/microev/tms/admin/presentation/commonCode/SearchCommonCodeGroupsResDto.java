package com.obigo.microev.tms.admin.presentation.commonCode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchCommonCodeGroupsResDto {
    private String comCodeGroupCd;
    private String comCodeGroupName;
}
