package com.obigo.microev.tms.core.domain.mapper.vo.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchAdminsResult {
    private Long adminSeq;
    private String email;
    private String adminName;
    private String contact;
    private String position;
    private LocalDateTime lastAccessedAt;
    private String statusCd;
    private String statusCdName;
}
