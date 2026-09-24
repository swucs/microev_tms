package com.obigo.microev.tms.core.domain.mapper.vo.delivery;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DeliveryStatisticsByDayResult {
    private LocalDate deliveryDate;
    private Integer totalCount;
    private Integer deliveryCompletedCount;
    private Integer deliveryUncompletedCount;
    private Integer collectionCompletedCount;
    private Integer collectionUncompletedCount;
    private BigDecimal processRatio;
    private Integer workMinutes;

}
