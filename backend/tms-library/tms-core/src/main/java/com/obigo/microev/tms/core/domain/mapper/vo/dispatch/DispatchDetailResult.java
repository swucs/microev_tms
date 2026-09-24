package com.obigo.microev.tms.core.domain.mapper.vo.dispatch;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DispatchDetailResult {
    private Long dispatchSeq;
    private String dispatchName;
    private LocalDate deliveryDate;
    private Long centerSeq;
    private String centerName;
    private String dispatchStatusCd;
    private String dispatchStatusCdName;
}
