package com.obigo.microev.tms.admin.presentation.dispatch.excel;

import com.obigo.microev.tms.admin.excel.ExcelError;
import com.obigo.microev.tms.admin.excel.ExcelParser;
import io.micrometer.common.util.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DeliveryExcelParser extends ExcelParser<DeliveryForExcel> {
    private static final String dateTimePattern = "yyyy-MM-dd HH:mm";
    private static final String timePattern = "HH:mm";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(timePattern);

    public DeliveryExcelParser(Sheet sheet) {
        super(sheet);
    }


    @Override
    protected List<ExcelError> validate(int rowNumber, Row row) {
        if (row == null) {
            return null;
        }

        errors = new ArrayList<>();

        int colNumber = 0;
        String headerName = "운송장번호";
        Cell cell = row.getCell(colNumber);
        checkRequired(rowNumber, colNumber, cell, headerName);
        checkMaxLength(rowNumber, colNumber, cell, headerName, 50);

        colNumber = 1;
        headerName = "수령인 이름";
        cell = row.getCell(colNumber);
        checkRequired(rowNumber, colNumber, cell, headerName);
        checkMaxLength(rowNumber, colNumber, cell, headerName, 30);

        colNumber = 2;
        headerName = "수령인 연락처";
        cell = row.getCell(colNumber);
        checkRequired(rowNumber, colNumber, cell, headerName);
        checkMaxLength(rowNumber, colNumber, cell, headerName, 20);

        colNumber = 3;
        headerName = "배송지 우편번호";
        cell = row.getCell(colNumber);
        checkRequired(rowNumber, colNumber, cell, headerName);
        checkMaxLength(rowNumber, colNumber, cell, headerName, 10);
        checkNumeric(rowNumber, colNumber, cell, headerName);

        colNumber = 4;
        headerName = "배송지 주소(도로명/지번)";
        cell = row.getCell(colNumber);
        checkRequired(rowNumber, colNumber, cell, headerName);
        checkMaxLength(rowNumber, colNumber, cell, headerName, 100);

        colNumber = 5;
        headerName = "배송지 상세주소";
        cell = row.getCell(colNumber);
        checkRequired(rowNumber, colNumber, cell, headerName);
        checkMaxLength(rowNumber, colNumber, cell, headerName, 100);

        colNumber = 6;
        headerName = "차량ID";
        cell = row.getCell(colNumber);
        checkRequired(rowNumber, colNumber, cell, headerName);
        checkNumeric(rowNumber, colNumber, cell, headerName);

        colNumber = 7;
        headerName = "상품명";
        cell = row.getCell(colNumber);
        checkRequired(rowNumber, colNumber, cell, headerName);
        checkMaxLength(rowNumber, colNumber, cell, headerName, 100);

        colNumber = 8;
        headerName = "박스 수량";
        cell = row.getCell(colNumber);
        checkRequired(rowNumber, colNumber, cell, headerName);
        checkNumeric(rowNumber, colNumber, cell, headerName);

        colNumber = 9;
        headerName = "배송유형(배송/수거)";
        cell = row.getCell(colNumber);
        checkRequired(rowNumber, colNumber, cell, headerName);
        String value = getStringValue(cell);
        if (StringUtils.isNotBlank(value)) {
            if (!value.equals("배송") && !value.equals("수거")) {
                errors.add(new ExcelError(rowNumber, colNumber, headerName, value, "배송유형은 '배송' 또는 '수거'만 가능합니다."));
            }
        }

        colNumber = 10;
        headerName = "배송메모";
        cell = row.getCell(colNumber);
        checkMaxLength(rowNumber, colNumber, cell, headerName, 1000);

        colNumber = 11;
        headerName = "출입번호";
        cell = row.getCell(colNumber);
        checkMaxLength(rowNumber, colNumber, cell, headerName, 50);

        colNumber = 12;
        headerName = "집하일시";
        cell = row.getCell(colNumber);
        checkDateTimeFormat(rowNumber, colNumber, cell, headerName, dateTimeFormatter, dateTimePattern);

        colNumber = 13;
        headerName = "배송예정시간(시작)";
        cell = row.getCell(colNumber);
        checkTimeFormat(rowNumber, colNumber, cell, headerName, timeFormatter, timePattern);

        colNumber = 14;
        headerName = "배송예정시간(종료)";
        cell = row.getCell(colNumber);
        checkTimeFormat(rowNumber, colNumber, cell, headerName, timeFormatter, timePattern);


        return errors;
    }


    @Override
    protected DeliveryForExcel parseRow(Row row) {
        if (row == null) {
            return null;
        }

        DeliveryForExcel deliveryForExcel = new DeliveryForExcel();
        deliveryForExcel.setTrackingNum(getStringValue(row.getCell(0)));
        deliveryForExcel.setRecipientName(getStringValue(row.getCell(1)));
        deliveryForExcel.setRecipientPhoneNum(getStringValue(row.getCell(2)));
        deliveryForExcel.setDeliveryPostalCode(getStringValue(row.getCell(3)));
        deliveryForExcel.setDeliveryAddr1(getStringValue(row.getCell(4)));
        deliveryForExcel.setDeliveryAddr2(getStringValue(row.getCell(5)));
        deliveryForExcel.setVehicleSeq(getLongValue(row.getCell(6)));
        deliveryForExcel.setProductName(getStringValue(row.getCell(7)));
        deliveryForExcel.setBoxCount(getIntegerValue(row.getCell(8)));
        deliveryForExcel.setDeliveryStatusCdName(getStringValue(row.getCell(9)));
        deliveryForExcel.setMemo(getStringValue(row.getCell(10)));
        deliveryForExcel.setSecurityCode(getStringValue(row.getCell(11)));
        deliveryForExcel.setPickupDatetime(getLocalDateTimeValue(row.getCell(12), dateTimeFormatter));
        deliveryForExcel.setDeliveryEstimatedStartTime(getLocalTimeValue(row.getCell(13), timeFormatter));
        deliveryForExcel.setDeliveryEstimatedEndTime(getLocalTimeValue(row.getCell(14), timeFormatter));

        return deliveryForExcel;
    }


}
