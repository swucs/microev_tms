package com.obigo.microev.tms.admin.vo;

import com.obigo.microev.tms.core.domain.enumeration.AdminStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrentAdmin {
    private Long adminSeq;
    private String email;
    private String userName;
    private String contact;
    private String position;
    private AdminStatus status;
}
