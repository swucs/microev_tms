package com.obigo.microev.tms.core.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class Driver {
    private Long driverSeq;
    private String driverName;
    private Long centerSeq;
    private String loginId;
    private String password;
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
    private LocalDateTime createdAt;
    private Long creatorSeq;
    private LocalDateTime modifiedAt;
    private Long modifierSeq;
}
