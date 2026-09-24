package com.obigo.microev.tms.admin.presentation.commonCode;


import lombok.Data;

@Data
public class GetValidCommonCodesResDto {
    private String comCodeGroupCd;
    private String comCodeCd;
    private String comCodeName;
    private Integer sortOrder;
}
