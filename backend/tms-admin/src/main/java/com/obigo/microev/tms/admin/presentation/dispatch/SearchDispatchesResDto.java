package com.obigo.microev.tms.admin.presentation.dispatch;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchDispatchesResDto {
    private Long dispatchSeq;
    private String dispatchName;
    private LocalDate deliveryDate;
    private Long centerSeq;
    private String centerName;
    private String dispatchStatusCd;
    private String dispatchStatusCdName;
    private String deliveryCount;
    private String deliveryVehicleCount;
}
