package com.obigo.microev.tms.api.presentation.inspection;

import lombok.Data;

import java.util.List;

@Data
public class GetMobileInspectionResDto {

    private int totalBoxCount;
    private int inspectionCompletedCount;
    private int inspectionWaitingCount;

    private List<Inspection> inspections;

    @Data
    public static class Inspection {
        private Long deliverySeq;
        private String trackingNum;
        private String deliveryAddr1;
        private String deliveryAddr2;
        private Integer boxCount;
        private String productName;
        private String deliveryStatusCd;
        private String deliveryStatusCdName;
    }
}
