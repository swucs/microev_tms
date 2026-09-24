package com.obigo.microev.tms.core.domain.mapper.vo.center;

import lombok.Data;

@Data
public class SearchCentersResult {
    private Long centerSeq;
    private String centerName;
    private String centerTypeCd;
    private String centerTypeCdName;
    private String zipCode;
    private String centerAddr1;
    private String centerAddr2;
    private String managerName;
    private String contactNum;
    private String usageYn;
    private Integer vehicleCount;
}
