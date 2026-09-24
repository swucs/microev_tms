package com.obigo.microev.tms.core.domain.mapper.vo.driver;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class SearchDriversResult {
    private Long driverSeq;
    private String driverName;
    private Long centerSeq;
    private String centerName;
    private String loginId;
    private String extSystemLinkedId;
    private String driverPhoneNum;
    private String email;
    private String driverLicenseNum;
    private LocalDate driverLicenseDate;
    private String driverLicenseAgency;
    private String compName;
    private String workingDays;
    private LocalTime workingStartHour;
    private LocalTime workingEndHour;
    private String statusCd;
    private String statusCdName;
    private Long creatorSeq;
    private String creatorName;
    private LocalDateTime createdAt;
}
