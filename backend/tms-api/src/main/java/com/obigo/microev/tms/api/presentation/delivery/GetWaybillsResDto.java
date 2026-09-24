package com.obigo.microev.tms.api.presentation.delivery;

import lombok.Data;

import java.util.List;

@Data
public class GetWaybillsResDto {

    private String address;
    private Integer totalBoxCount;
    private Integer deliveryBoxCount;
    private Integer collectionBoxCount;
    private List<Waybill> waybills;

    @Data
    public static class Waybill {
        private Long deliverySeq;
        private String trackingNum;
        private String productName;
        private Integer boxCount;
        private String memo;
        private String securityCode;
        private String deliveryAddr1;
        private String deliveryAddr2;
        private String deliveryStatusCd;
        private String deliveryStatusCdName;
        private String deliveryTypeCd;
        private String deliveryTypeCdName;
        private String waybillStatusName;
        private String shootingImpossibleYn;
        private String shootingImpossibleReasonCd;
        private String shootingImpossibleReasonCdName;
        private String consignmentLocationCd;
        private String consignmentLocationCdName;
        private String uncompletedReasonCd;
        private String uncompletedReasonCdName;
        private Long deliveryPhotoAttachSeq;
        private String deliveryPhotoFullPath;
    }
}
