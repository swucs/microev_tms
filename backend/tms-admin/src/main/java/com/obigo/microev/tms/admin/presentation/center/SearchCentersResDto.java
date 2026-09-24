package com.obigo.microev.tms.admin.presentation.center;

import lombok.Data;

@Data
public class SearchCentersResDto {
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
