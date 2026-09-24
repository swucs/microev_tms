package com.obigo.microev.tms.core.domain.mapper.vo.admin;

import lombok.Data;

@Data
public class SearchAdminsCondition {
    private String email;
    private String adminName;
    private String statusCd;
}
