package com.obigo.microev.tms.core.domain.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegionRange {
    private Long regionSeq;
    private String eupMyeonDong;
}