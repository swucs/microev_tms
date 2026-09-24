package com.obigo.microev.tms.core.domain.mapper.vo.delivery;

import lombok.Data;

@Data
public class DeliveryCountByDriverSeqResult {
    private Long dispatchSeq;
    private Integer totalCount;
    private Integer deliveryTotalCount;
    private Integer deliveryTotalCompletedCount;
    private Integer deliveryCompletedCount;
    private Integer deliveryUncompletedCount;
    private Integer collectionTotalCount;
    private Integer collectionTotalCompletedCount;
    private Integer collectionCompletedCount;
    private Integer collectionUncompletedCount;
}
