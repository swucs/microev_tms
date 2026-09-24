package com.obigo.microev.tms.admin.presentation.driver;

import com.obigo.microev.tms.core.domain.validator.common.ValidCommonCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateDriverReqDto {

    @Schema(description = "기사명")
    @NotBlank(message = "{validation.driver.driverName.notBlank}")
    private String driverName;

    @Schema(description = "센터시퀀스")
    @NotNull(message = "{validation.driver.centerSeq.notNull}")
    private Long centerSeq;

    @Schema(description = "로그인 ID")
    @NotBlank(message = "{validation.driver.loginId.notBlank}")
    private String loginId;

    @Schema(description = "비밀번호")
    @NotBlank(message = "{validation.driver.password.notBlank}")
    private String password;

    @Schema(description = "외부시스템연동ID")
    @NotBlank(message = "{validation.driver.extSystemLinkedId.notBlank}")
    private String extSystemLinkedId;

    @Schema(description = "기사 연락처")
    @NotBlank(message = "{validation.driver.driverPhoneNum.notBlank}")
    private String driverPhoneNum;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "운전면허번호")
    private String driverLicenseNum;

    @Schema(description = "운전면허 발급일")
    private LocalDate driverLicenseDate;

    @Schema(description = "운전면허 발급기관")
    private String driverLicenseAgency;

    @Schema(description = "회사명")
    private String compName;

    @Schema(description = "근무요일", allowableValues = {"월", "화", "수", "목", "금", "토", "일"}, example = "월화수목금")
    private String workingDays;

    @Schema(description = "근무시작시간")
    private LocalTime workingStartHour;

    @Schema(description = "근무종료시간")
    private LocalTime workingEndHour;

    @Schema(description = "상태", allowableValues = {"WORKING", "ABSENCE", "RETIRED"}, example = "WORKING")
    @ValidCommonCode(groupCode = "DriverStatus")
    private String statusCd;
}
