package com.obigo.microev.tms.admin.service;

import com.obigo.microev.tms.admin.presentation.dispatch.AutoDispatchReqDto;
import com.obigo.microev.tms.admin.presentation.dispatch.AutoDispatchResDto;
import com.obigo.microev.tms.admin.presentation.dispatch.CreateDeliveryReqDto;
import com.obigo.microev.tms.admin.presentation.dispatch.CreateDispatchReqDto;
import com.obigo.microev.tms.admin.presentation.dispatch.CreateExcelFileForDeliveryResDto;
import com.obigo.microev.tms.admin.presentation.dispatch.DispatchDetailResDto;
import com.obigo.microev.tms.admin.presentation.dispatch.ModifyDeliveryReqDto;
import com.obigo.microev.tms.admin.presentation.dispatch.ModifyDispatchReqDto;
import com.obigo.microev.tms.admin.presentation.dispatch.ModifyStatusToDispatchedReqDto;
import com.obigo.microev.tms.admin.presentation.dispatch.SearchDispatchesReqDto;
import com.obigo.microev.tms.admin.presentation.dispatch.SearchDispatchesResDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DispatchService {
    List<SearchDispatchesResDto> searchDispatches(SearchDispatchesReqDto reqDto);

    DispatchDetailResDto getDispatchDetail(long dispatchSeq);

    long createDispatch(CreateDispatchReqDto reqDto);

    void modifyDispatch(Long dispatchSeq, ModifyDispatchReqDto reqDto);

    void modifyStatusToDispatched(Long dispatchSeq, ModifyStatusToDispatchedReqDto reqDto);

    void modifyStatusToWaiting(Long dispatchSeq);

    AutoDispatchResDto autoDispatch(AutoDispatchReqDto reqDto);

    void removeDispatch(Long dispatchSeq);

    long createDelivery(CreateDeliveryReqDto reqDto);

    void modifyDelivery(Long deliverySeq, ModifyDeliveryReqDto reqDto);

    void removeDelivery(Long deliverySeq);

    CreateExcelFileForDeliveryResDto createExcelFileForSampleDelivery(Long dispatchSeq);

    void readAndSaveDeliveryExcel(Long dispatchSeq, MultipartFile file);

    // 실제 Delivery 정보를 엑셀로 변환하여 반환
    CreateExcelFileForDeliveryResDto createExcelFileForDelivery(Long dispatchSeq);
}
