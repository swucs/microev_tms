package com.obigo.microev.tms.admin.presentation.dispatch.excel;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class DeliveryForExcel {
    private String trackingNum;
    private String recipientName;
    private String recipientPhoneNum;
    private String deliveryPostalCode;
    private String deliveryAddr1;
    private String deliveryAddr2;
    private Long vehicleSeq;
    private String productName;
    private Integer boxCount;
    private String deliveryStatusCdName;
    private String memo;
    private String securityCode;
    private LocalDateTime pickupDatetime;
    private LocalTime deliveryEstimatedStartTime;
    private LocalTime deliveryEstimatedEndTime;
}
