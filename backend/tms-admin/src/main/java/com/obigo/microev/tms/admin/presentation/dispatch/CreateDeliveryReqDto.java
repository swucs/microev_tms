package com.obigo.microev.tms.admin.presentation.dispatch;

import com.obigo.microev.tms.core.domain.validator.common.ValidDate;
import com.obigo.microev.tms.core.domain.validator.common.ValidTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateDeliveryReqDto {
    
    @Schema(description = "배차시퀀스")
    @NotNull(message = "{validation.dispatch.dispatchSeq.notNull}")
    private Long dispatchSeq;

    @Schema(description = "권역시퀀스 : 우편번호 검색시 권역검색 필요함")
    private Long regionSeq;

    @Schema(description = "배송번호")
    @NotBlank(message = "{validation.dispatch.trackingNum.notBlank}")
    @Size(max = 50, message = "{validation.dispatch.trackingNum.size}")
    private String trackingNum;
    
    @Schema(description = "수령인 이름")
    @NotBlank(message = "{validation.dispatch.recipientName.notBlank}")
    private String recipientName;

    @Schema(description = "수령인 연락처")
    @NotBlank(message = "{validation.dispatch.recipientPhoneNum.notBlank}")
    private String recipientPhoneNum;

    @Schema(description = "배송지 우편번호")
    @NotBlank(message = "{validation.dispatch.deliveryPostalCode.notBlank}")
    private String deliveryPostalCode;

    @Schema(description = "배송지 주소")
    @NotBlank(message = "{validation.dispatch.deliveryAddr1.notBlank}")
    private String deliveryAddr1;

    @Schema(description = "배송지 상세주소")
    @NotBlank(message = "{validation.dispatch.deliveryAddr2.notBlank}")
    private String deliveryAddr2;

    @Schema(description = "상품명")
    @NotBlank(message = "{validation.dispatch.productName.notBlank}")
    private String productName;

    @Schema(description = "위도")
    @NotNull(message = "{validation.center.latitude.notNull}")
    private BigDecimal latitude;

    @Schema(description = "경도")
    @NotNull(message = "{validation.center.longitude.notNull}")
    private BigDecimal longitude;

    @Schema(description = "박스수량")
    @NotNull(message = "{validation.dispatch.boxCount.notNull}")
    private Integer boxCount;

    @Schema(description = "배송메모")
    private String memo;

    @Schema(description = "출입번호")
    private String securityCode;

    @Schema(description = "배송유형")
    @NotBlank(message = "{validation.dispatch.deliveryTypeCd.notBlank}")
    private String deliveryTypeCd;

    @Schema(description = "배송예정시작시간")
    @ValidTime
    private String deliveryEstimatedStartTime;

    @Schema(description = "배송예정종료시간")
    @ValidTime
    private String deliveryEstimatedEndTime;

    @Schema(description = "차량시퀀스")
    @NotNull(message = "{validation.dispatch.vehicleSeq.notNull}")
    private Long vehicleSeq;
    
    @Schema(description = "집하일시")
    @ValidDate(pattern = "yyyy-MM-dd HH:mm", message = "{validation.dispatch.collectionDate.notValid}")
    private String pickupDatetime;
}
