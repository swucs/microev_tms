package com.obigo.microev.tms.api.converter;


import com.obigo.microev.tms.api.presentation.delivery.GetDeliveryStatusResDto;
import com.obigo.microev.tms.api.presentation.delivery.GetMainResDto;
import com.obigo.microev.tms.api.presentation.delivery.GetWaybillsResDto;
import com.obigo.microev.tms.api.presentation.inspection.GetMobileInspectionResDto;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.DeliveryCountByDriverSeqResult;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.DeliveryStatusByDriverSeqResult;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.WaybillResult;
import com.obigo.microev.tms.core.domain.mapper.vo.dispatch.DispatchDetailResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InspectionConverter {
    List<GetMobileInspectionResDto.Inspection> GetMobileInspectionResDtoInspection(List<WaybillResult> waybillResults);
}
