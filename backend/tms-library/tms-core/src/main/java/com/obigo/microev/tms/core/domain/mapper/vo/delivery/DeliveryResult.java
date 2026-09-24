package com.obigo.microev.tms.core.domain.mapper.vo.delivery;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class DeliveryResult {
    private Long deliverySeq;
    private Long vehicleSeq;
    private String vehicleName;
    private String driverName;
    private Long regionSeq;
    private String regionName;
    private Integer deliveryOrder;
    private String trackingNum;
    private String recipientName;
    private String recipientPhoneNum;
    private String deliveryPostalCode;
    private String deliveryAddr1;
    private String deliveryAddr2;
    private String deliveryTypeCd;
    private String deliveryTypeCdName;
    private String productName;
    private Integer boxCount;
    private String memo;
    private String securityCode;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String deliveryStatusCd;
    private String deliveryStatusCdName;
    private LocalDateTime pickupDatetime;
    private LocalTime deliveryEstimatedStartTime;
    private LocalTime deliveryEstimatedEndTime;
    private LocalDateTime deliveryDatetime;
    private String shootingImpossibleYn;
    private String shootingImpossibleReasonCd;
    private String shootingImpossibleReasonCdName;
    private String consignmentLocationCd;
    private String consignmentLocationCdName;
    private String uncompletedReasonCd;
    private String uncompletedReasonCdName;
    private Long deliveryPhotoAttachSeq;
    private String deliveryPhotoFileName;
    private String deliveryPhotoFullPath;
    private LocalDateTime createdAt;
}
