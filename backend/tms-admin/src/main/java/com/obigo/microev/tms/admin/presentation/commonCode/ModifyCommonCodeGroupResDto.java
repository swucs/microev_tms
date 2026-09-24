package com.obigo.microev.tms.admin.presentation.commonCode;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModifyCommonCodeGroupResDto {

    private String comCodeGroupCd;
    private String comCodeGroupName;
}