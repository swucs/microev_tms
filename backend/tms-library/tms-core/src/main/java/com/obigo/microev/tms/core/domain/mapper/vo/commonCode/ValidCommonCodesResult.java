package com.obigo.microev.tms.core.domain.mapper.vo.commonCode;


import lombok.Data;

@Data
public class ValidCommonCodesResult {
    private String comCodeGroupCd;
    private String comCodeCd;
    private String comCodeName;
    private Integer sortOrder;
}
