package com.obigo.microev.tms.core.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiCall {
    public Long apiCallSeq;
    private String apiUrl;
    private String httpMethod;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long driverSeq;
    private String responseCode;
    private LocalDateTime createdAt;
}
