package com.obigo.microev.tms.admin.excel;

import com.obigo.microev.tms.admin.exception.ExcelParseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
abstract public class ExcelParser<T> {

    private int dataStartRow = 1; // Default starting row for data parsing
    private final Sheet sheet;
    private List<T> dataList;
    protected List<ExcelError> errors = null;

    protected ExcelParser(Sheet sheet) {
        this.sheet = sheet;
    }

    abstract protected T parseRow(Row row);

    abstract protected List<ExcelError> validate(int rowNumber, Row row);


    public List<T> parse() {
        List<ExcelError> allErrors = new ArrayList<>();
        int rowCount = sheet.getPhysicalNumberOfRows();

        // 엑셀 데이터 유효성 검사 (오류가 있더라도 끝까지 검증)
        for (int i = dataStartRow; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            List<ExcelError> errors = this.validate(i, row);
            if (errors != null) {
                allErrors.addAll(errors);
            }
        }

        // 유효성 검사에 에러 결과가 있으면 예외 발생
        if (!allErrors.isEmpty()) {
            log.error("Excel validation errors found: {}", allErrors);
            throw new ExcelParseException(allErrors);
        }

        // 엑셀 데이터 파싱
        this.dataList = new ArrayList<>();
        for (int i = dataStartRow; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            T data = this.parseRow(row);
            dataList.add(data);
        }
        
        // 중복 체크

        return dataList;
    }

    protected String getStringValue(Cell cell) {
        return cell == null ? null : StringUtils.trim(cell.toString());
    }


    protected LocalDateTime getLocalDateTimeValue(Cell cell, DateTimeFormatter dateTimeFormatter) {
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue();
        }
        String value = getStringValue(cell);
        if (StringUtils.isBlank(value)) {
            return null;
        }

        try {
            return LocalDateTime.parse(value, dateTimeFormatter);
        } catch (Exception e) {
            log.error("Error parsing LocalDateTime from cell: {}", value, e);
            return null;
        }
    }


    protected LocalTime getLocalTimeValue(Cell cell, DateTimeFormatter timeFormatter) {
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalTime();
        }
        String value = getStringValue(cell);
        if (StringUtils.isBlank(value)) {
            return null;
        }

        try {
            return LocalTime.parse(value, timeFormatter);
        } catch (Exception e) {
            log.warn("Error parsing LocalTime from cell: {}", value, e);
            return null;
        }
    }


    protected Integer getIntegerValue(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return Double.valueOf(cell.getNumericCellValue()).intValue();
        }
        String value = getStringValue(cell);
        if (StringUtils.isBlank(value)) {
            return null;
        }

        try {
            return Integer.parseInt(cell.getStringCellValue());
        } catch (Exception e) {
            log.warn("Error parsing Integer from cell: {}", value, e);
            return null;
        }
    }

    protected Long getLongValue(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return Double.valueOf(cell.getNumericCellValue()).longValue();
        }
        String value = getStringValue(cell);
        if (StringUtils.isBlank(value)) {
            return null;
        }

        try {
            return Long.parseLong(cell.getStringCellValue());
        } catch (Exception e) {
            log.warn("Error parsing Long from cell: {}", value, e);
            return null;
        }
    }


    protected void checkRequired(int rowNumber, int colNumber, Cell cell, String headerName) {
        String value = getStringValue(cell);

        if (StringUtils.isBlank(value)) {
            errors.add(new ExcelError(rowNumber, colNumber, headerName, value, "필수 입력값입니다."));
        }
    }

    protected void checkMaxLength(int rowNumber, int colNumber, Cell cell, String headerName, int maxLength) {
        String value = getStringValue(cell);
        if (StringUtils.isBlank(value)) return;

        if (StringUtils.length(value) > maxLength) {
            errors.add(new ExcelError(rowNumber, colNumber, headerName, value, "최대 " + maxLength + "자까지 입력 가능합니다."));
        }
    }

    protected void checkNumeric(int rowNumber, int colNumber, Cell cell, String headerName) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return;
        }

        String value = getStringValue(cell);
        if (StringUtils.isBlank(value)) return;

        try {
            Double.valueOf(value);
        } catch (NumberFormatException e) {
            errors.add(new ExcelError(rowNumber, colNumber, headerName, value, "숫자만 입력할 수 있습니다."));
        }
    }

    protected void checkDateTimeFormat(int rowNumber, int colNumber, Cell cell, String headerName, DateTimeFormatter dateTimeFormatter, String dateTimePattern) {
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            try {
                cell.getLocalDateTimeCellValue().format(dateTimeFormatter);
            } catch (Exception e) {
                errors.add(new ExcelError(rowNumber, colNumber, headerName, "", "날짜 형식이 올바르지 않습니다. [" + dateTimePattern + "]"));
            }
            return;
        }

        String value = getStringValue(cell);
        if (StringUtils.isBlank(value)) return;

        try {
            LocalDateTime.parse(value, dateTimeFormatter);
        } catch (Exception e) {
            errors.add(new ExcelError(rowNumber, colNumber, headerName, value, "날짜 형식이 올바르지 않습니다. [" + dateTimePattern + "]"));
        }
    }


    protected void checkTimeFormat(int rowNumber, int colNumber, Cell cell, String headerName, DateTimeFormatter timeFormatter, String timePattern) {
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            try {
                cell.getLocalDateTimeCellValue().toLocalTime().format(timeFormatter);
            } catch (Exception e) {
                errors.add(new ExcelError(rowNumber, colNumber, headerName, "", "시간 형식이 올바르지 않습니다. [" + timePattern + "]"));
            }
            return;
        }


        String value = getStringValue(cell);
        if (StringUtils.isBlank(value)) return;

        try {
            LocalTime.parse(value, timeFormatter);
        } catch (Exception e) {
            errors.add(new ExcelError(rowNumber, colNumber, headerName, value, "날짜 형식이 올바르지 않습니다. [" + timePattern + "]"));
        }
    }
}
