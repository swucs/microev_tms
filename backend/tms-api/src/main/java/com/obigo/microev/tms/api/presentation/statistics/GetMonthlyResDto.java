package com.obigo.microev.tms.api.presentation.statistics;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Data
public class GetMonthlyResDto {

    private Summary summary;
    private List<DailyStatistics> dailies;

    @Data
    public static class Summary {
        private Integer totalCount;
        private Integer deliveryCompletedCount;
        private Integer deliveryUncompletedCount;
        private Integer collectionCompletedCount;
        private Integer collectionUncompletedCount;
        private BigDecimal processRatio;
        private Integer workMinutes;
    }


    @Data
    public static class DailyStatistics {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate deliveryDate;
        private Integer totalCount;
        private Integer deliveryCompletedCount;
        private Integer deliveryUncompletedCount;
        private Integer collectionCompletedCount;
        private Integer collectionUncompletedCount;
        private BigDecimal processRatio;
        private Integer workMinutes;

        public String getDeliveryDayOfWeek() {
            return deliveryDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault());
        }
    }
}
