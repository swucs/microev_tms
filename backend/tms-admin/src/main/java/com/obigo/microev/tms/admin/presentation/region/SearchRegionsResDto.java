package com.obigo.microev.tms.admin.presentation.region;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchRegionsResDto {
    private Long regionSeq;
    private String regionName;
    private String eupMyeonDong;
    private String vehicleName;
    private String creatorName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
