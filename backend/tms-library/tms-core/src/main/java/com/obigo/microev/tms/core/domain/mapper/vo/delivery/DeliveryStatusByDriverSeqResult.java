package com.obigo.microev.tms.core.domain.mapper.vo.delivery;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class DeliveryStatusByDriverSeqResult {
    private String deliveryAddr1;
    private Integer totalBoxCount;
    private Integer completedBoxCount;
    private Integer deliveryBoxCount;
    private Integer collectionBoxCount;
    private LocalTime deliveryEstimatedStartTime;
    private LocalTime deliveryEstimatedEndTime;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String deliveryCompletedYn;
    private LocalDateTime deliveryDatetime;
}
