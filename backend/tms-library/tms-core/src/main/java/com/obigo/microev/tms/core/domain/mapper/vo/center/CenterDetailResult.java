package com.obigo.microev.tms.core.domain.mapper.vo.center;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CenterDetailResult {
    private Long centerSeq;
    private String centerName;
    private String centerTypeCd;
    private String centerTypeCdName;
    private String centerAddr1;
    private String centerAddr2;
    private String zipCode;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal totalArea;
    private String managerName;
    private String contactNum;
    private String usageYn;
    private LocalDateTime createdAt;
    private Long creatorSeq;
    private String creatorName;
    private Integer vehicleCount;
}
