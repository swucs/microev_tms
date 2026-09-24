package com.obigo.microev.tms.admin.presentation.dispatch;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class DispatchDetailResDto {
    private Long dispatchSeq;
    private Long centerSeq;
    private String centerName;
    private String dispatchName;
    private String dispatchStatusCd;
    private String dispatchStatusCdName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate deliveryDate;

    private List<DeliveryByVehicle> deliveriesByVehicle;


    @Data
    @AllArgsConstructor
    public static class DeliveryByVehicle {
        private String vehicleName;
        private List<Delivery> deliveries;
    }

    @Data
    public static class Delivery {
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

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime pickupDatetime;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime deliveryEstimatedStartTime;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime deliveryEstimatedEndTime;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime deliveryDatetime;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDateTime createdAt;
    }
}
