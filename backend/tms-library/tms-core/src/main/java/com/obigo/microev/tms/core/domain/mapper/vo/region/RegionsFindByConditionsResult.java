package com.obigo.microev.tms.core.domain.mapper.vo.region;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegionsFindByConditionsResult {
    private Long regionSeq;
    private String regionName;
    private String eupMyeonDong;
    private String vehicleName;
    private String creatorName;
    private LocalDateTime createdAt;
}
