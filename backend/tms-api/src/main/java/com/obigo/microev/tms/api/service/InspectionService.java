package com.obigo.microev.tms.api.service;

import com.obigo.microev.tms.api.presentation.inspection.GetMobileInspectionResDto;

public interface InspectionService {
    GetMobileInspectionResDto getInspections();

    void processInspectionCompleted(Long deliverySeq);

    void processInspectionCancel(Long deliverySeq);
}
