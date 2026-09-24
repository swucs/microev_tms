package com.obigo.microev.tms.api.service.impl;


import com.obigo.microev.tms.api.service.ApiCallService;
import com.obigo.microev.tms.core.domain.entity.ApiCall;
import com.obigo.microev.tms.core.domain.mapper.ApiCallMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ApiCallServiceImpl implements ApiCallService {

    private final ApiCallMapper apiCallMapper;

    /**
     * API 호출 로그 저장
     * @param apiCall
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public void createApiCall(ApiCall apiCall) {
        try {
            apiCall.setCreatedAt(LocalDateTime.now());
            apiCallMapper.insert(apiCall);
        } catch (Exception e) {
            log.info("ApiCall insert error", e);
        }
    }
}
