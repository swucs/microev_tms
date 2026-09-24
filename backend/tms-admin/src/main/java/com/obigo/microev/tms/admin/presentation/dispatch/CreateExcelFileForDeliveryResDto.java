package com.obigo.microev.tms.admin.presentation.dispatch;

import lombok.Builder;
import lombok.Data;
import org.apache.poi.ss.usermodel.Workbook;

@Data
@Builder
public class CreateExcelFileForDeliveryResDto {
    private Workbook workbook;
    private String fileName;
}
