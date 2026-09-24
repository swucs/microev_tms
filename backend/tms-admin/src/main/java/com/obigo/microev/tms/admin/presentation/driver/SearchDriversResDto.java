package com.obigo.microev.tms.admin.presentation.driver;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class SearchDriversResDto {
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime workingStartHour;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime workingEndHour;
    private String statusCd;
    private String statusCdName;
    private Long creatorSeq;
    private String creatorName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
