package com.obigo.microev.tms.admin.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
public class ExcelError {
    private int rowNumber;
    private Integer columnNumber;
    private String headerName;
    private String value;
    private String errorMessage;

    public String getTotalErrorMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(rowNumber + 1).append("행");
        if (StringUtils.isNotBlank(headerName)) {
            sb.append(" : ").append(headerName);
        }
        sb.append("] ").append(errorMessage);

        if (StringUtils.isNotBlank(value)) {
            sb.append(" (입력값:").append(value).append(")");
        }
        return sb.toString();
    }
}
