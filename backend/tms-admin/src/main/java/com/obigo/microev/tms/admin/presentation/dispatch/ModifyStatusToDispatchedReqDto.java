package com.obigo.microev.tms.admin.presentation.dispatch;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ModifyStatusToDispatchedReqDto {

    @Schema(description = "배송순서목록")
    @NotEmpty(message = "{validation.dispatch.deliveryOrders.notEmpty}")
    @Valid
    private List<DeliveryOrder> deliveryOrders;

    @Data
    public static class DeliveryOrder {

        @Schema(description = "차량시퀀스")
        @NotNull(message = "{validation.dispatch.deliveryOrder.notNull}")
        private Long vehicleSeq;

        @Schema(description = "배송시퀀스 목록")
        @NotEmpty(message = "{validation.dispatch.deliverySeqList.notEmpty}")
        private List<Long> deliverySeqList;

    }

}
