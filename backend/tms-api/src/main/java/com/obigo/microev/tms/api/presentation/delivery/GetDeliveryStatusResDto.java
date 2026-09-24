package com.obigo.microev.tms.api.presentation.delivery;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class GetDeliveryStatusResDto {
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

    private List<DeliverySummary> deliverySummaries;

    @Data
    public static class DeliverySummary {
        private String deliveryAddr1;
        private Integer totalBoxCount;
        private Integer completedBoxCount;
        private Integer deliveryBoxCount;
        private Integer collectionBoxCount;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime deliveryEstimatedStartTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime deliveryEstimatedEndTime;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private String deliveryCompletedYn;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime deliveryDatetime;
    }
}
