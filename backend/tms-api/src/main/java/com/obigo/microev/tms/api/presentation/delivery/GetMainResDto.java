package com.obigo.microev.tms.api.presentation.delivery;

import lombok.Data;

@Data
public class GetMainResDto {
    private Long dispatchSeq;
    private String driverDeliveryStatus;
    private int totalCount;
    private int deliveryTotalCount;
    private int deliveryTotalCompletedCount;
    private int deliveryCompletedCount;
    private int deliveryUncompletedCount;
    private int collectionTotalCount;
    private int collectionTotalCompletedCount;
    private int collectionCompletedCount;
    private int collectionUncompletedCount;
}
