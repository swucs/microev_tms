package com.obigo.microev.tms.core.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DeliveryHistory {
    private Long deliveryHistorySeq;
    private Long deliverySeq;
    private Long dispatchSeq;
    private Long driverSeq;
    private String deliveryStatusCd;
    private LocalDateTime createdAt;
    private Long creatorSeq;
}
