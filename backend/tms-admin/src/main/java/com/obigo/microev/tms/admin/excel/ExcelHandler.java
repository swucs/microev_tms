package com.obigo.microev.tms.admin.excel;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class ExcelHandler {

    @Getter
    private final XSSFWorkbook workbook;

    private Font fontRed;
    private Font fontNormal;
    private CellStyle styleHeader;

    private CellStyle numberStyleData;
    private CellStyle floatStyleData;
    private CellStyle dateTimeStyleData;
    private CellStyle textStyleData;

    public ExcelHandler() {
        workbook = new XSSFWorkbook();

        createStyleHeader();
        createStyleData(); // 데이터 스타일 생성
    }

    private void createStyleHeader() {
        // 헤더 스타일
        styleHeader = workbook.createCellStyle();
        styleHeader.setWrapText(true);
        styleHeader.setAlignment(HorizontalAlignment.CENTER);
        styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
        styleHeader.setBorderLeft(BorderStyle.THIN);
        styleHeader.setBorderRight(BorderStyle.THIN);
        styleHeader.setBorderTop(BorderStyle.THIN);
        styleHeader.setBorderBottom(BorderStyle.THIN);
        styleHeader.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);

        // 필수 헤더 글자 폰트
        fontRed = workbook.createFont();
        fontRed.setColor(IndexedColors.RED.getIndex());
        fontRed.setFontName("맑은 고딕");
        fontRed.setFontHeightInPoints((short) 9);

        // 일반 헤더 글자 폰트
        fontNormal = workbook.createFont();
        fontNormal.setFontName("맑은 고딕");
        fontNormal.setFontHeightInPoints((short) 9);
        styleHeader.setFont(fontNormal);
    }

    private void createStyleData() {

        // 데이터 스타일
        CellStyle styleData = workbook.createCellStyle();
        styleData.setWrapText(true);
        styleData.setVerticalAlignment(VerticalAlignment.CENTER);
        styleData.setBorderLeft(BorderStyle.THIN);
        styleData.setBorderRight(BorderStyle.THIN);
        styleData.setBorderTop(BorderStyle.THIN);
        styleData.setBorderBottom(BorderStyle.THIN);

        // 글자 폰트
        Font fontData = workbook.createFont();
        fontData.setFontName("맑은 고딕");
        fontData.setFontHeightInPoints((short) 9);
        styleData.setFont(fontData);


        short numberFormat = workbook.createDataFormat().getFormat("0");
        short floatFormat = workbook.createDataFormat().getFormat("0.###");
        short dateTimeFormat = workbook.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss");
        short textFormat = workbook.createDataFormat().getFormat("@");

        // 숫자 스타일
        numberStyleData = workbook.createCellStyle();
        numberStyleData.cloneStyleFrom(styleData);
        numberStyleData.setDataFormat(numberFormat);

        // 실수 스타일
        floatStyleData = workbook.createCellStyle();
        floatStyleData.cloneStyleFrom(styleData);
        floatStyleData.setDataFormat(floatFormat);

        // 날짜/시간 스타일
        dateTimeStyleData = workbook.createCellStyle();
        dateTimeStyleData.cloneStyleFrom(styleData);
        dateTimeStyleData.setDataFormat(dateTimeFormat);

        // 텍스트 스타일
        textStyleData = workbook.createCellStyle();
        textStyleData.cloneStyleFrom(styleData);
        textStyleData.setDataFormat(textFormat);
    }

    public Sheet createSheet(String sheetName) {
        return workbook.createSheet(sheetName);
    }

    /**
     * 헤더 셀 생성
     * @param sheet
     * @param rowIndex
     * @param colIndex
     * @param headerNames
     */
    public void createHeaderCell(Sheet sheet, int rowIndex, int colIndex, String[] headerNames) {
        Row headerRow = sheet.createRow(rowIndex);
        for (int i = 0; i < headerNames.length; i++) {
            Cell cell = headerRow.createCell(colIndex + i);
            String headerName = headerNames[i];
            if (StringUtils.startsWith(headerName, "* ")) {
                // 필수 항목인경우 [*] 빨간색 표시
                XSSFRichTextString richText = new XSSFRichTextString(headerName);
                richText.applyFont(0, 1, fontRed);
                richText.applyFont(1, StringUtils.length(headerName), fontNormal);
                cell.setCellValue(richText);
            } else {
                cell.setCellValue(headerNames[i]);
            }

            cell.setCellStyle(styleHeader);
        }
    }


    /**
     * 데이터 셀 생성
     * @param sheet
     * @param rowIndex
     * @param colIndex
     * @param dataList
     */
    public void createDataCell(Sheet sheet, int rowIndex, int colIndex, List<Object[]> dataList) {
        for (Object[] rowData : dataList) {
            Row row = sheet.createRow(rowIndex);
            for (int j = 0; j < rowData.length; j++) {
                createCell(row, colIndex + j, rowData[j]);
                sheet.autoSizeColumn(colIndex + j);
            }
            rowIndex++;
        }

    }


    public <T> void createCell(Row row, int columnCount, T value) {
        Cell cell = row.createCell(columnCount);
        switch (value) {
            case null -> {
                cell.setBlank();
                cell.setCellStyle(textStyleData);
            }
            case Integer i -> {
                cell.setCellValue(i);
                cell.setCellStyle(numberStyleData);
            }
            case Long l -> {
                cell.setCellValue(l);
                cell.setCellStyle(numberStyleData);
            }
            case Float v -> {
                cell.setCellValue(v);
                cell.setCellStyle(floatStyleData);
            }
            case Double v -> {
                cell.setCellValue(v);
                cell.setCellStyle(floatStyleData);
            }
            case LocalDateTime localDateTime -> {
                cell.setCellValue(localDateTime);
                cell.setCellStyle(dateTimeStyleData);
            }
            default -> {
                cell.setCellValue(String.valueOf(value));
                cell.setCellStyle(textStyleData);
            }
        }
    }


//    public byte[] createExcelFile() {
//        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//            workbook.write(outputStream);
//            return outputStream.toByteArray();
//        } catch (IOException e) {
//            log.error("Error creating Excel file", e);
//            return null; // Ensure a return value in case of exception
//        } finally {
//            try {
//                workbook.close(); // Ensure workbook is closed
//            } catch (IOException e) {
//                log.error("Error closing workbook", e);
//            }
//        }
//    }


//    public void saveExcelFile() {
//        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//            workbook.write(outputStream);
//
//            //File 저장
//
//            // Implement file saving logic here
//            byte[] excelBytes = outputStream.toByteArray();
//            FileOutputStream fos = new FileOutputStream("d:/test.xlsx");
//            fos.write(excelBytes);
//            fos.close();
//        } catch (IOException e) {
//            log.error("Error creating Excel file", e);
//        } finally {
//            try {
//                workbook.close(); // Ensure workbook is closed
//            } catch (IOException e) {
//                log.error("Error closing workbook", e);
//            }
//        }
//    }

}
