package com.obigo.microev.tms.admin.presentation.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchAdminsResDto {
    private Long adminSeq;
    private String email;
    private String adminName;
    private String contact;
    private String position;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastAccessedAt;
    private String statusCd;
    private String statusCdName;
}
