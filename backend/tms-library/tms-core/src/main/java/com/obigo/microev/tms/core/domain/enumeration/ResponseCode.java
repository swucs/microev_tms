package com.obigo.microev.tms.core.domain.enumeration;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Getter
public enum ResponseCode {

    //성공
    SUCCESS("SUCCESS"),   //성공

    SERVER_EXCEPTION("E0001"),   //처리 중 Exception이 발생하였습니다.
    MISSING_REQUIRED_VALUE("E0002"),  //필수 값이 누락되었습니다.
    NOT_FOUND_DB_DATA("E0003"),    //DB에 해당 데이터가 존재하지 않습니다.
    INVALID_REQUEST_INFO("E0004"),  //요청 정보에 오류가 있습니다.
    NOT_FOUND_RESOURCE("E0005"),            //존재하지 않는 리소스입니다.
    TYPE_MISMATCH_REQUEST_INFO("E0006"),    //요청 정보의 데이터 타입이 일치하지 않습니다.
    INVALID_ENUM_CLASS("E0007"),    //유효한 Enum 클래스가 아닙니다.
    FILE_UPLOAD_FAILED("E0008"),    //파일 업로드에 실패하였습니다.
    EXCEL_PARSE_FAILED("E0009"),    //엑셀 파일 읽기를 실패 하였습니다.
    EXCEL_UPLOAD_FAILED("E0010"),    //엑셀 파일을 업로드하여 저장하던 중 실패하였습니다.


    INVALID_ACCOUNT_ID("E0100"), //잘못된 계정ID 입니다.
    LOCKED_ADMIN_ID("E0101"),   //해당 ID는 잠금 상태 입니다. 관리자에게 문의 하시길 바랍니다.
    DISABLED_ADMIN_ID("E0102"),  //해당 ID는 탈퇴 상태 입니다. 관리자에게 문의 하시길 바랍니다.
    DORMANT_ADMIN_ID("E0103"),  //해당 ID는 휴면 상태 입니다. 관리자에게 문의 하시길 바랍니다.
    WRONG_PASSWORD_COUNT("E0104"),    //로그인 정보가 잘못되었습니다.\n %d회 실패하였습니다. %d회 이상시 잠금 상태가 됩니다.
    OUT_OF_DATE_PASSWORD("E0105"),   //유효 기간이 지난 PASSWORD 입니다.
    REQUIRED_UPDATE_PASSWORD("E0106"),   //최초 로그인 입니다. 비밀번호 변경이 필요합니다.

    INVALID_CURRENT_PWD("E0107"),    //현재 패스워드와 일치하지 않습니다.
    DUPLICATED_PASSWORD("E0108"),   //새로운 패스워드는 기존 패스워드와 동일할 수 없습니다.
    INVALID_PASSWORD_CONTAINS_ADMIN_ID("E0109"),  //패스워드에 관리자 ID를 포함해서는 안 됩니다.
    INVALID_PASSWORD_LENGTH("E0110"),  //패스워드는 8자리 이상이어야 합니다.
    INVALID_PASSWORD_CONTAINS_SAME_CHAR("E0111"),   //패스워드에는 동일한 문자가 3번 이상 사용될 수 없습니다.
    INVALID_PASSWORD_CONTAINS_SPECIAL_CHAR("E0112"),  //패스워드에는 영문, 숫자, 특수문자가 포함되어 있어야 합니다.
    INVALID_REQUEST_VALUE("E0113"), //요청 값이 올바르지 않습니다.
    UNAUTHORIZED("E0114"),    //권한이 부족합니다.
    UNAUTHORIZED_MENU("E0116"),   //메뉴에 대한 접근 권한이 부족합니다.
    EMPTY_ID_OR_PASSSWORD("E0117"), //아이디 또는 비밀번호가 입력되지 않았습니다.
    INVALID_DATE_FORMAT("E0118"), // 일치 하지 않는 날짜 포맷입니다. (yyyy-MM-dd)
    REQUIRED_LOGIN("E0119"),        //로그인이 필요합니다.

    //토큰 (E0120~)
    EXPIRED_TOKEN("E0120"), //토큰이 만료되었습니다.
    CHECK_TOKEN("E0121"),   //인증 토큰을 확인 해 주세요.
    JWT_REFRESH_INVALID_TOKEN("E0122"),   //재발급 토큰이 일치하지 않습니다.
    INVALID_TOKEN("E0123"),   //토큰이 올바르지 않습니다.
    INVALID_REFRESH_TOKEN("E0124"),   //재발급 토큰이 올바르지 않습니다. 다시 로그인 하세요.

    WRONG_PASSWORD("E0130"),    //로그인 정보가 잘못되었습니다.


    //권역 (E0200~)
    DUPLICATED_EUP_MYUN_DONG("E0200"),   //중복된 읍면동이 존재합니다.
    NOT_FOUND_REGION("E0201"),   //존재하지 않는 권역입니다.
    EXIST_VEHICLE_IN_REGION("E0202"),   //해당 권역에 등록된 차량이 존재하므로 삭제할 수 없습니다.

    //센터 (E0300~)
    NOT_FOUND_CENTER("E0300"),           //존재하지 않는 센터입니다.
    EXIST_VEHICLE_IN_CENTER("E0301"),    //해당 센터에 등록된 차량이 존재하므로 삭제할 수 없습니다.

    //차량 (E0400~)
    DUPLICATED_VEHICLE_NUM("E0400"),        //중복된 차량번호가 존재합니다.
    DUPLICATED_VEHICLE_REG_NUM("E0401"),    //중복된 차량등록번호가 존재합니다.
    NOT_FOUND_VEHICLE("E0402"),             //존재하지 않는 차량입니다.
    NOT_MATCHED_DRIVER_CENTER("E0403"),     //해당 기사의 센터와 일치하지 않습니다. 동일한 센터의 기사를 선택해주세요.
    DUPLICATED_VEHICLE_DRIVER("E0404"),     //해당 기사가 이미 차량에 소속되어 있습니다. 차량에서 해당 기사를 제외 후 다시 시도해주세요.
    CANNOT_CHANGE_DRIVER_PROCESSING_DISPATCH("E0405"),          //해당 차량이 진행중인 배차가 존재하므로 기사를 변경할 수 없습니다.


    //기사 (E0500~)
    DUPLICATED_DRIVER_LOGIN_ID("E0500"),    //중복된 Login ID가 존재합니다.
    NOT_FOUND_DRIVER("E0501"),              //존재하지 않는 기사입니다.
    EXIST_VEHICLE_IN_DRIVER("E0502"),       //해당 기사가 소속된 차량이 존재하므로 삭제할 수 없습니다. 해당 차량의 기사를 변경 후 삭제하세요.
    DUPLICATED_DRIVER_EXT_SYSTEM_LINKED_ID("E0503"),    //중복된 외부시스템연동ID가 존재합니다.
    NOT_ALLOWED_UPDATE_CENTER_EXIST_VEHICLE("E0504"),       //현재 소속된 차량이 존재하므로 센터를 변경할 수 없습니다.


    //공통코드 (E0600~)
    DUPLICATED_COMMON_CODE_GROUP("E0600"),   //이미 존재하는 공통코드그룹 입니다.
    NOT_FOUND_COMMON_CODE_GROUP("E0601"),    //존재하지 않는 공통코드그룹 입니다.
    DUPLICATED_COMMON_CODE("E0602"),        //이미 존재하는 공통코드 입니다.
    NOT_FOUND_COMMON_CODE("E0603"),         //존재하지 않는 공통코드 입니다.
    DELETE_EXISTS_COMMON_CODE("E0604"),     //하위 공통코드가 존재하므로 삭제할 수 없습니다.

    //관리자 (E0700~)
    DUPLICATED_EMAIL("E0700"),      //중복된 Email이 존재합니다.
    NOT_FOUND_ADMIN("E0701"),       //존재하지 않는 관리자입니다.

    //배차 (E0800~)
    NOT_FOUND_DISPATCH("E0800"),    //존재하지 않는 배차입니다.
    INVALID_REQUEST_DISPATCH_STATUS("E0801"),   //배송중 또는 배송완료 상태이므로 처리할 수 없습니다.
    NOT_FOUND_DELIVERY("E0802"),     //존재하지 않는 배송정보입니다.
    INVALID_CENTER_TYPE("E0803"),        //해당 센터유형이 슈퍼가 아니므로 데이터를 변경할 수 없습니다.
    NOT_ASSIGNED_VEHICLE_DRIVER("E0804"),        //해당 차량에 기사가 배정되지 않았습니다.
    INVALID_REQUEST_DISPATCH_STATUS2("E0805"),   //배차확정, 배송중 또는 배송완료 상태이므로 처리할 수 없습니다.
    DUPLICATED_TRACKING_NUM("E0806"),   //중복된 운송장번호가 존재합니다.
    INVALID_VEHICLE("E0807"),   //차량정보가 올바르지 않습니다.


    //API (E0900~)
    INVALID_STATISTICS_SEARCH_YYYYMM("E0900"),   //통계 조회 년월이 올바르지 않습니다. (yyyyMM)
    IMPOSSIBLE_INSPECTION_NOT_INSPECTION_WAITING("E0901"),   //검수대기 상태가 아니므로 검수완료 처리할 수 없습니다.
    IMPOSSIBLE_INSPECTION_CANCEL_NOT_INSPECTION_COMPLETED("E0902"),   //검수취소는 검수완료 상태에서 가능합니다.
    IMPOSSIBLE_INSPECTION_CANCEL_IN_DELIVERY("E0903"),   //이미 배송을 시작했으므로 검수취소 처리할 수 없습니다.
    EXISTS_INSPECTION_WAITING("E0904"),   //검수대기 상태인 배송정보가 존재합니다.
    NOT_MATCH_DRIVER("E0905"),   //해당 배송정보의 배송기사가 아니므로 처리할 수 없습니다.
    NOT_IN_DELIVERY("E0906"),   //배송중 상태가 아니므로 처리할 수 없습니다.
    DELIVERY_PHOTO_FILE_NOT_FOUND("E0907"),   //배송사진 파일이 업로드 되지 않았습니다.
    ;





    private final String code;
    private final String messageCode;
    private String message;

    ResponseCode(String code) {
        this.code = code;
        this.messageCode = "common.responseCode." + code;
    }


    @Component
    @RequiredArgsConstructor
    public static class ResponseCodeEnumInjector {
        private final MessageSource messageSource;

        @PostConstruct
        public void postConstruct() {
            for (ResponseCode responseCode : ResponseCode.values()) {
                responseCode.message = messageSource.getMessage(responseCode.messageCode, null, LocaleContextHolder.getLocale());
            }
        }
    }
}
