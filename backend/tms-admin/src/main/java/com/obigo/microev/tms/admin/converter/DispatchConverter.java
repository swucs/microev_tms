package com.obigo.microev.tms.admin.converter;


import com.obigo.microev.tms.admin.presentation.dispatch.AutoDispatchResDto;
import com.obigo.microev.tms.admin.presentation.dispatch.CreateDeliveryReqDto;
import com.obigo.microev.tms.admin.presentation.dispatch.CreateDispatchReqDto;
import com.obigo.microev.tms.admin.presentation.dispatch.DispatchDetailResDto;
import com.obigo.microev.tms.admin.presentation.dispatch.ModifyDeliveryReqDto;
import com.obigo.microev.tms.admin.presentation.dispatch.ModifyDispatchReqDto;
import com.obigo.microev.tms.admin.presentation.dispatch.SearchDispatchesReqDto;
import com.obigo.microev.tms.admin.presentation.dispatch.SearchDispatchesResDto;
import com.obigo.microev.tms.admin.presentation.dispatch.excel.DeliveryForExcel;
import com.obigo.microev.tms.admin.utils.AuthenticationUtils;
import com.obigo.microev.tms.core.domain.entity.Delivery;
import com.obigo.microev.tms.core.domain.entity.Dispatch;
import com.obigo.microev.tms.core.domain.enumeration.AdminStatus;
import com.obigo.microev.tms.core.domain.enumeration.DeliveryType;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.DeliveryResult;
import com.obigo.microev.tms.core.domain.mapper.vo.dispatch.DispatchDetailResult;
import com.obigo.microev.tms.core.domain.mapper.vo.dispatch.SearchDispatchesCondition;
import com.obigo.microev.tms.core.domain.mapper.vo.dispatch.SearchDispatchesResult;
import com.obigo.microev.tms.core.domain.mapper.vo.vehicle.VehicleByEupMyeonDongResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE
        , imports = {LocalDateTime.class, LocalDate.class, LocalTime.class
                    , AuthenticationUtils.class, AdminStatus.class, DateTimeFormatter.class
                    , DeliveryType.class
        }
)
public interface DispatchConverter {



    @Mapping(target = "deliveryDate", expression = "java(LocalDate.parse(reqDto.getDeliveryDate(), DateTimeFormatter.ofPattern(\"yyyy-MM-dd\")))")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "creatorSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    Dispatch toDispatch(CreateDispatchReqDto reqDto);

    @Mapping(target = "deliveryDate", expression = "java(LocalDate.parse(reqDto.getDeliveryDate(), DateTimeFormatter.ofPattern(\"yyyy-MM-dd\")))")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    void updateDispatch(@MappingTarget Dispatch dispatch, ModifyDispatchReqDto reqDto);

    SearchDispatchesCondition toSearchDispatchesCondition(SearchDispatchesReqDto reqDto);

    SearchDispatchesResDto toSearchDispatchesResDto(SearchDispatchesResult searchDispatchesResult);

    DispatchDetailResDto toDispatchDetailResDto(DispatchDetailResult detailResult);

    DispatchDetailResDto.Delivery toDispatchDetailResDtoDelivery(DeliveryResult deliveryResult);

    @Mapping(target = "vehicleName", expression = "java(result.getVehicleName() + \"(\" + result.getDriverName() + \")\")")
    AutoDispatchResDto toAutoDispatchResDto(VehicleByEupMyeonDongResult result);


    @Mapping(target = "deliveryEstimatedStartTime", expression = "java(LocalTime.parse(reqDto.getDeliveryEstimatedStartTime(), DateTimeFormatter.ofPattern(\"HH:mm\")))")
    @Mapping(target = "deliveryEstimatedEndTime", expression = "java(LocalTime.parse(reqDto.getDeliveryEstimatedEndTime(), DateTimeFormatter.ofPattern(\"HH:mm\")))")
    @Mapping(target = "pickupDatetime", expression = "java(LocalDateTime.parse(reqDto.getPickupDatetime(), DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm\")))")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "creatorSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    Delivery toDelivery(CreateDeliveryReqDto reqDto);

    @Mapping(target = "deliveryEstimatedStartTime", expression = "java(LocalTime.parse(reqDto.getDeliveryEstimatedStartTime(), DateTimeFormatter.ofPattern(\"HH:mm\")))")
    @Mapping(target = "deliveryEstimatedEndTime", expression = "java(LocalTime.parse(reqDto.getDeliveryEstimatedEndTime(), DateTimeFormatter.ofPattern(\"HH:mm\")))")
    @Mapping(target = "pickupDatetime", expression = "java(LocalDateTime.parse(reqDto.getPickupDatetime(), DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm\")))")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    void updateDelivery(@MappingTarget Delivery delivery, ModifyDeliveryReqDto reqDto);

    @Mapping(target = "deliveryTypeCd", expression = "java(\"배송\".equals(deliveryForExcel.getDeliveryStatusCdName()) ? DeliveryType.DELIVERY.name() : DeliveryType.COLLECTION.name())")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "creatorSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    Delivery toDelivery(DeliveryForExcel deliveryForExcel);
}
