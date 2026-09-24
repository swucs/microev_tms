package com.obigo.microev.tms.admin.presentation;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> implements Serializable {
    private String resultCode;
    private String resultMessage;
    private T data;
}
