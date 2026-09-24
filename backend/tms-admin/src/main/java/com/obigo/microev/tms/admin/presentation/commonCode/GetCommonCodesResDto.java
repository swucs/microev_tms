package com.obigo.microev.tms.admin.presentation.commonCode;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class GetCommonCodesResDto {
    private Long comCodeSeq;
    private String comCodeGroupCd;
    private String comCodeCd;
    private String comCodeName;
    private Integer sortOrder;
    private String usageYn;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private Long creatorSeq;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;
    private Long modifierSeq;
}