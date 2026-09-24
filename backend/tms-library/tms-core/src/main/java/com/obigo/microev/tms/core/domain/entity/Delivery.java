package com.obigo.microev.tms.core.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class Delivery {
    private Long deliverySeq;
    private Long dispatchSeq;
    private Long vehicleSeq;
    private Long driverSeq;
    private Long regionSeq;
    private Integer deliveryOrder;
    private String trackingNum;
    private String recipientName;
    private String recipientPhoneNum;
    private String deliveryPostalCode;
    private String deliveryAddr1;
    private String deliveryAddr2;
    private String deliveryTypeCd;
    private String productName;
    private Integer boxCount;
    private String memo;
    private String securityCode;
    private LocalDateTime pickupDatetime;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String deliveryStatusCd;
    private LocalTime deliveryEstimatedStartTime;
    private LocalTime deliveryEstimatedEndTime;
    private LocalDateTime deliveryDatetime;
    private String shootingImpossibleYn;
    private String shootingImpossibleReasonCd;
    private String consignmentLocationCd;
    private String uncompletedReasonCd;
    private Long deliveryPhotoAttachSeq;
    private LocalDateTime createdAt;
    private Long creatorSeq;
    private LocalDateTime modifiedAt;
    private Long modifierSeq;
}
