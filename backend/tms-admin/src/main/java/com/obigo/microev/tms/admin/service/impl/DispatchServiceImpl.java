package com.obigo.microev.tms.admin.service.impl;

import com.obigo.microev.tms.admin.converter.DispatchConverter;
import com.obigo.microev.tms.admin.excel.ExcelError;
import com.obigo.microev.tms.admin.excel.ExcelHandler;
import com.obigo.microev.tms.admin.exception.ExcelUploadException;
import com.obigo.microev.tms.admin.presentation.common.GetCoordinateReqDto;
import com.obigo.microev.tms.admin.presentation.common.GetCoordinateResDto;
import com.obigo.microev.tms.admin.presentation.common.GetEupMyeonDongReqDto;
import com.obigo.microev.tms.admin.presentation.common.GetEupMyeonDongResDto;
import com.obigo.microev.tms.admin.presentation.dispatch.AutoDispatchReqDto;
import com.obigo.microev.tms.admin.presentation.dispatch.AutoDispatchResDto;
import com.obigo.microev.tms.admin.presentation.dispatch.CreateDeliveryReqDto;
import com.obigo.microev.tms.admin.presentation.dispatch.CreateDispatchReqDto;
import com.obigo.microev.tms.admin.presentation.dispatch.CreateExcelFileForDeliveryResDto;
import com.obigo.microev.tms.admin.presentation.dispatch.DispatchDetailResDto;
import com.obigo.microev.tms.admin.presentation.dispatch.ModifyDeliveryReqDto;
import com.obigo.microev.tms.admin.presentation.dispatch.ModifyDispatchReqDto;
import com.obigo.microev.tms.admin.presentation.dispatch.ModifyStatusToDispatchedReqDto;
import com.obigo.microev.tms.admin.presentation.dispatch.SearchDispatchesReqDto;
import com.obigo.microev.tms.admin.presentation.dispatch.SearchDispatchesResDto;
import com.obigo.microev.tms.admin.presentation.dispatch.excel.DeliveryExcelParser;
import com.obigo.microev.tms.admin.presentation.dispatch.excel.DeliveryForExcel;
import com.obigo.microev.tms.admin.service.CommonService;
import com.obigo.microev.tms.admin.service.DispatchService;
import com.obigo.microev.tms.admin.utils.AuthenticationUtils;
import com.obigo.microev.tms.core.domain.entity.Center;
import com.obigo.microev.tms.core.domain.entity.Delivery;
import com.obigo.microev.tms.core.domain.entity.Dispatch;
import com.obigo.microev.tms.core.domain.enumeration.CenterType;
import com.obigo.microev.tms.core.domain.enumeration.DeliveryStatus;
import com.obigo.microev.tms.core.domain.enumeration.DispatchStatus;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import com.obigo.microev.tms.core.domain.mapper.CenterMapper;
import com.obigo.microev.tms.core.domain.mapper.DeliveryMapper;
import com.obigo.microev.tms.core.domain.mapper.DispatchMapper;
import com.obigo.microev.tms.core.domain.mapper.VehicleMapper;
import com.obigo.microev.tms.core.domain.mapper.vo.delivery.DeliveryResult;
import com.obigo.microev.tms.core.domain.mapper.vo.dispatch.DispatchDetailResult;
import com.obigo.microev.tms.core.domain.mapper.vo.dispatch.SearchDispatchesCondition;
import com.obigo.microev.tms.core.domain.mapper.vo.vehicle.BelongingVehicleResult;
import com.obigo.microev.tms.core.domain.mapper.vo.vehicle.VehicleByEupMyeonDongResult;
import com.obigo.microev.tms.core.exception.BusinessException;
import com.obigo.microev.tms.core.exception.InvalidRequestException;
import com.obigo.microev.tms.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DispatchServiceImpl implements DispatchService {
    private final CommonService commonService;

    private final DispatchMapper dispatchMapper;
    private final DeliveryMapper deliveryMapper;
    private final VehicleMapper vehicleMapper;
    private final CenterMapper centerMapper;

    private final DispatchConverter dispatchConverter;


    public static final String TRACKING_NUM_PREFIX = "TMS";


    /**
     * 배차 검색 조회
     * @param reqDto
     * @return
     */
    @Override
    public List<SearchDispatchesResDto> searchDispatches(SearchDispatchesReqDto reqDto) {

        SearchDispatchesCondition condition = dispatchConverter.toSearchDispatchesCondition(reqDto);

        return dispatchMapper.findByConditions(condition).stream()
                .map(dispatchConverter::toSearchDispatchesResDto)
                .toList();
    }


    /**
     * 배차 상세 조회
     * @param dispatchSeq
     * @return
     */
    @Override
    public DispatchDetailResDto getDispatchDetail(long dispatchSeq) {
        //배차정보 조회
        DispatchDetailResult detailResult = dispatchMapper.findDetailById(dispatchSeq);
        if (detailResult == null) {
            throw new NotFoundException(ResponseCode.NOT_FOUND_DISPATCH);
        }

        DispatchDetailResDto resDto = dispatchConverter.toDispatchDetailResDto(detailResult);

        //배송정보 조회
        List<DeliveryResult> deliveryResults = deliveryMapper.findByDispatchSeq(dispatchSeq);
        //차량별로 분류한 MAP
        Map<Long, List<DispatchDetailResDto.Delivery>> mapByVehicleSeq = deliveryResults.stream()
                .map(dispatchConverter::toDispatchDetailResDtoDelivery)
                .collect(Collectors.groupingBy(DispatchDetailResDto.Delivery::getVehicleSeq));


        //해당 센터의 차량목록 조회
        List<DispatchDetailResDto.DeliveryByVehicle> deliveriesByVehicle = new ArrayList<>();
        List<BelongingVehicleResult> vehicles = vehicleMapper.findByCenterSeq(detailResult.getCenterSeq());

        //차량별로 배송정보를 매핑
        for (BelongingVehicleResult vehicle : vehicles) {
            Long vehicleSeq = vehicle.getVehicleSeq();
            String vehicleName = vehicle.getVehicleName() + " (" + vehicle.getDriverName() + ")";

            List<DispatchDetailResDto.Delivery> deliveries = mapByVehicleSeq.getOrDefault(vehicleSeq, Collections.emptyList());
            deliveriesByVehicle.add(new DispatchDetailResDto.DeliveryByVehicle(vehicleName, deliveries));
        }
        resDto.setDeliveriesByVehicle(deliveriesByVehicle);

        return resDto;
    }

    /**
     * 배차 생성
     * @param reqDto
     * @return
     */
    @Override
    public long createDispatch(CreateDispatchReqDto reqDto) {
        Dispatch dispatch = dispatchConverter.toDispatch(reqDto);

        //초기상태: 배차대기
        dispatch.setDispatchStatusCd(DispatchStatus.DISPATCH_WAITING.name());

        // insert
        dispatchMapper.insert(dispatch);
        return dispatch.getDispatchSeq();
    }

    /**
     * 배차 수정
     * @param dispatchSeq
     * @param reqDto
     */
    @Override
    public void modifyDispatch(Long dispatchSeq, ModifyDispatchReqDto reqDto) {
        Dispatch dispatch = dispatchMapper.findById(dispatchSeq)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_DISPATCH));

        //센터유형이 슈퍼가 아닌 경우 데이터 수정 불가
        this.checkCenterTypeSuper(dispatch.getCenterSeq());

        //배차상태가 배송중이거나 배송완료인 경우 수정 불가
        this.checkDispatchStatusAfterDelivery(dispatch.getDispatchStatusCd());

        dispatchConverter.updateDispatch(dispatch, reqDto);
        dispatchMapper.update(dispatch);
    }

    /**
     * 센터유형이 슈퍼가 아닌 경우 Exception
     * @param centerSeq
     */
    private void checkCenterTypeSuper(Long centerSeq) {
        Center center = centerMapper.findById(centerSeq);
        if (!CenterType.SUPERMARKET.name().equals(center.getCenterTypeCd())) {
            throw new InvalidRequestException(ResponseCode.INVALID_CENTER_TYPE);
        }
    }

    /**
     * 배차상태가 배송 이후의 상태 (배송중이거나 배송완료)인 경우 Exception
     * @param dispatchStatusCd
     */
    private void checkDispatchStatusAfterDelivery(String dispatchStatusCd) {
        if (DispatchStatus.IN_DELIVERY.name().equals(dispatchStatusCd)
                || DispatchStatus.COMPLETED.name().equals(dispatchStatusCd)) {
            throw new InvalidRequestException(ResponseCode.INVALID_REQUEST_DISPATCH_STATUS);
        }
    }


    /**
     * 배차상태가 배차확정 이후의 상태(배차확정, 배송중, 배송완료)인 경우 Exception
     * @param dispatchStatusCd
     */
    private void checkDispatchStatusAfterDispatched(String dispatchStatusCd) {
        if (DispatchStatus.DISPATCHED.name().equals(dispatchStatusCd)
                || DispatchStatus.IN_DELIVERY.name().equals(dispatchStatusCd)
                || DispatchStatus.COMPLETED.name().equals(dispatchStatusCd)) {
            throw new InvalidRequestException(ResponseCode.INVALID_REQUEST_DISPATCH_STATUS2);
        }
    }




    /**
     * 배차 상태를 배차확정로 변경
     * @param dispatchSeq
     */
    @Override
    public void modifyStatusToDispatched(Long dispatchSeq, ModifyStatusToDispatchedReqDto reqDto) {
        //배차 정보 조회
        Dispatch dispatch = dispatchMapper.findById(dispatchSeq)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_DISPATCH));

        //센터유형이 슈퍼가 아닌 경우 데이터 수정 불가
        this.checkCenterTypeSuper(dispatch.getCenterSeq());

        //배차상태가 배차확정/배송중이거나 배송완료인 경우 수정 불가
        this.checkDispatchStatusAfterDispatched(dispatch.getDispatchStatusCd());


        //배차 상태 변경
        dispatch.setDispatchStatusCd(DispatchStatus.DISPATCHED.name());
        dispatchMapper.update(dispatch);
        
        
        //배송정보 순서 저장
        LocalDateTime now = LocalDateTime.now();
        Long adminSeq = Objects.requireNonNull(AuthenticationUtils.getCurrentAdmin()).getAdminSeq();

        reqDto.getDeliveryOrders()
                .forEach(order -> {
                    int deliveryOrder = 0;

                    for (Long deliverySeq : order.getDeliverySeqList()) {
                        Delivery delivery = Delivery.builder()
                                .deliverySeq(deliverySeq)
                                .deliveryOrder(deliveryOrder++)
                                .modifiedAt(now)
                                .modifierSeq(adminSeq)
                                .build();
                        deliveryMapper.updateDeliveryOrder(delivery);
                    }
                });
    }


    /**
     * 배차 상태를 배차대기로 변경
     * @param dispatchSeq
     */
    @Override
    public void modifyStatusToWaiting(Long dispatchSeq) {
        Dispatch dispatch = dispatchMapper.findById(dispatchSeq)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_DISPATCH));

        //센터유형이 슈퍼가 아닌 경우 데이터 수정 불가
        this.checkCenterTypeSuper(dispatch.getCenterSeq());

        //배차상태가 배송중이거나 배송완료인 경우 수정 불가
        this.checkDispatchStatusAfterDelivery(dispatch.getDispatchStatusCd());

        dispatch.setDispatchStatusCd(DispatchStatus.DISPATCH_WAITING.name());
        dispatchMapper.update(dispatch);
    }


    /**
     * 자동 배차
     * 주소로 권역을 찾아서 해당 권역의 차량을 배차한다.
     * @param reqDto
     * @return
     */
    @Override
    public AutoDispatchResDto autoDispatch(AutoDispatchReqDto reqDto) {

        //센터유형이 슈퍼가 아닌 경우 조회불가
        this.checkCenterTypeSuper(reqDto.getCenterSeq());

        //도로명 주소를 읍면동으로 변환
        GetEupMyeonDongResDto eupMyeonDongResDto = null;
        try {
            eupMyeonDongResDto = commonService.getEupMyeonDong(GetEupMyeonDongReqDto.of(reqDto.getRoadAddress()));
            if (eupMyeonDongResDto == null) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        String eupMyeonDong = eupMyeonDongResDto.getEupMyeonDong();
        if (StringUtils.isBlank(eupMyeonDong)) {
            return null;
        }
        
        //읍명동으로 차량 검색
        Optional<VehicleByEupMyeonDongResult> vehicleResultOptional = vehicleMapper.findByCenterSeqAndEupMyeonDong(
                reqDto.getCenterSeq(), eupMyeonDong
        );
        if (vehicleResultOptional.isEmpty()) {
            return null;
        }

        VehicleByEupMyeonDongResult vehicle = vehicleResultOptional.get();
        return dispatchConverter.toAutoDispatchResDto(vehicle);
    }


    /**
     * 배차 삭제
     * @param dispatchSeq
     */
    @Override
    public void removeDispatch(Long dispatchSeq) {
        Dispatch dispatch = dispatchMapper.findById(dispatchSeq)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_DISPATCH));

        //배차상태가 배송중이거나 배송완료인 경우 삭제 불가
        this.checkDispatchStatusAfterDelivery(dispatch.getDispatchStatusCd());

        //배송정보 삭제
        deliveryMapper.deleteByDispatchSeq(dispatchSeq);

        //배차정보 삭제
        dispatchMapper.delete(dispatchSeq);
    }


    /**
     * 배송 생성
     * @param reqDto
     * @return
     */
    @Override
    public long createDelivery(CreateDeliveryReqDto reqDto) {
        Delivery delivery = dispatchConverter.toDelivery(reqDto);
        return this.createDelivery(delivery);
    }


    /**
     * 배송 생성 실제 로직
     * @param delivery
     * @return
     */
    private long createDelivery(Delivery delivery) {
        Dispatch dispatch = dispatchMapper.findById(delivery.getDispatchSeq())
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_DISPATCH));

        //센터유형이 슈퍼가 아닌 경우 데이터 등록 불가
        //슈퍼가 아닌 택배로 실사하기로 결정했으므로 아래 로직은 주석처리(2025.05.29)
//        this.checkCenterTypeSuper(dispatch.getCenterSeq());

        //배차상태가 배송중이거나 배송완료인 경우 수정 불가
        this.checkDispatchStatusAfterDelivery(dispatch.getDispatchStatusCd());

        //현재 차량의 기사정보
        vehicleMapper.findById(delivery.getVehicleSeq())
                .ifPresentOrElse(
                        vehicle -> {
                            if (vehicle.getDriverSeq() == null) {
                                throw new InvalidRequestException(ResponseCode.NOT_ASSIGNED_VEHICLE_DRIVER);
                            }

                            //vehicleSeq 검증 (해당 배차의 센터와 차량의 센터가 일치하는지 확인)
                            if (!vehicle.getCenterSeq().equals(dispatch.getCenterSeq())) {
                                throw new InvalidRequestException(ResponseCode.INVALID_VEHICLE);
                            }

                            delivery.setDriverSeq(vehicle.getDriverSeq());
                        },
                        () -> {
                            throw new InvalidRequestException(ResponseCode.NOT_FOUND_VEHICLE);
                        }
                );

        //자동 배송번호 채번 (ex: TMS20240701-1234567)
        //운송장번호는 입력받기로 변경되었으므로 아래 로직은 주석처리(2025.05.29)
//        delivery.setTrackingNum(createNewTrackingNum());

        //trackingNum 중복체크 (2025.05.29 로직 추가)
        String trackingNum = deliveryMapper.findTrackingNum(delivery.getTrackingNum());
        if (StringUtils.isNotBlank(trackingNum)) {
            throw new InvalidRequestException(ResponseCode.DUPLICATED_TRACKING_NUM);
        }

        //배송순서 채번
        delivery.setDeliveryOrder(createNewDeliveryOrder(delivery.getDispatchSeq()));

        //초기상태 : 검수대기
        delivery.setDeliveryStatusCd(DeliveryStatus.INSPECTION_WAITING.name());

        deliveryMapper.insert(delivery);
        return delivery.getDeliverySeq();
    }

    /**
     * 자동 배송번호 채번 (ex: TMS20240701-1234567)
     * @return
     */
    @Deprecated
    private String createNewTrackingNum() {
        String yyyyMMdd = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String maxTrackingNum = deliveryMapper.findMaxTrackingNum(TRACKING_NUM_PREFIX + yyyyMMdd);
        long maxTrackingNumber = NumberUtils.toLong(StringUtils.right(maxTrackingNum, 7));

        return TRACKING_NUM_PREFIX + yyyyMMdd + "-" + StringUtils.leftPad(String.valueOf(maxTrackingNumber + 1), 7, "0");
    }

    /**
     * 배송순서 채번
     * @param dispatchSeq
     * @return
     */
    private Integer createNewDeliveryOrder(Long dispatchSeq) {
        Integer maxDeliveryOrder = deliveryMapper.findMaxDeliveryOrder(dispatchSeq);
        return maxDeliveryOrder == null ? 1 : maxDeliveryOrder + 1;
    }


    /**
     * 배송 수정
     * @param deliverySeq
     * @param reqDto
     */
    @Override
    public void modifyDelivery(Long deliverySeq, ModifyDeliveryReqDto reqDto) {
        Delivery delivery = deliveryMapper.findById(deliverySeq)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_DELIVERY));

        Dispatch dispatch = dispatchMapper.findById(delivery.getDispatchSeq())
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_DISPATCH));


        //센터유형이 슈퍼가 아닌 경우 데이터 수정 불가
        //슈퍼가 아닌 택배로 실사하기로 결정했으므로 아래 로직은 주석처리(2025.05.29)
//        this.checkCenterTypeSuper(dispatch.getCenterSeq());

        //배차상태가 배송중이거나 배송완료인 경우 수정 불가
        this.checkDispatchStatusAfterDelivery(dispatch.getDispatchStatusCd());

        //운송장번호가 변경된 경우 중복체크
        if (!StringUtils.equals(delivery.getTrackingNum(), reqDto.getTrackingNum())) {
            String trackingNum = deliveryMapper.findTrackingNum(reqDto.getTrackingNum());
            if (StringUtils.isNotBlank(trackingNum)) {
                throw new InvalidRequestException(ResponseCode.DUPLICATED_TRACKING_NUM);
            }
        }

        dispatchConverter.updateDelivery(delivery, reqDto);

        //현재 차량의 기사정보
        vehicleMapper.findById(delivery.getVehicleSeq())
                .ifPresent(vehicle -> {
                    if (vehicle.getDriverSeq() == null) {
                        throw new InvalidRequestException(ResponseCode.NOT_ASSIGNED_VEHICLE_DRIVER);
                    }
                    delivery.setDriverSeq(vehicle.getDriverSeq());
                });

        deliveryMapper.update(delivery);
    }


    /**
     * 배송 삭제
     * @param deliverySeq
     */
    @Override
    public void removeDelivery(Long deliverySeq) {
        Delivery delivery = deliveryMapper.findById(deliverySeq)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_DELIVERY));

        Dispatch dispatch = dispatchMapper.findById(delivery.getDispatchSeq())
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_DISPATCH));

        //센터유형이 슈퍼가 아닌 경우 데이터 수정 불가
        //슈퍼가 아닌 택배로 실사하기로 결정했으므로 아래 로직은 주석처리(2025.05.29)
//        this.checkCenterTypeSuper(dispatch.getCenterSeq());

        //배차상태가 배송중이거나 배송완료인 경우 수정 불가
        this.checkDispatchStatusAfterDelivery(dispatch.getDispatchStatusCd());

        deliveryMapper.delete(deliverySeq);
    }


    @Override
    public CreateExcelFileForDeliveryResDto createExcelFileForSampleDelivery(Long dispatchSeq) {
        ExcelHandler excelHandler = new ExcelHandler();

        // 배송정보 시트 생성
        List<Object[]> dataList = new ArrayList<>();
        dataList.add(new Object[]{
                "TMS20240701-1234567", "홍길동", "010-1234-5678", "12345", "서울특별시 강남구 테헤란로 123",
                "강남역 1번출구", 1, "나이키 운동화 265", 3, "배송", "배송메모 예시", "303#1234",
                "2025-05-14 10:00", "15:00", "16:00"});
        this.createExcelSheetForDelivery(excelHandler, dataList);

        // 차량기준정보 시트 생성
        this.createExcelSheetForVehicleBasis(dispatchSeq, excelHandler);

        // 엑셀 파일명
        Dispatch dispatch = dispatchMapper.findById(dispatchSeq)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_DISPATCH));
        String fileName = dispatch.getDispatchName() + "_" + dispatch.getDeliveryDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "_샘플.xlsx";
        log.info("Excel file name: {}", fileName);

        return CreateExcelFileForDeliveryResDto.builder()
                .workbook(excelHandler.getWorkbook())
                .fileName(fileName)
                .build();
    }


    /**
     * 배송정보 시트 생성
     * @param excelHandler
     */
    private void createExcelSheetForDelivery(ExcelHandler excelHandler, List<Object[]> dataList) {
        Sheet sheet = excelHandler.createSheet("배송정보");
        String[] headerNames = {"* 운송장번호", "* 수령인 이름", "* 수령인 연락처", "* 배송지 우편번호", "* 배송지 주소(도로명/지번)"
                , "* 배송지 상세주소", "* 차량ID\n(차량기준정보 시트 참고)", "* 상품명", "* 박스 수량", "* 배송유형\n(배송/수거)"
                , "배송메모", "출입번호", "집하일시", "배송예정시간(시작)", "배송예정시간(종료)"
        };
        excelHandler.createHeaderCell(sheet, 0, 0, headerNames);

        // 데이터 셀 생성
        excelHandler.createDataCell(sheet, 1, 0, dataList);
    }


    /**
     * 차량기준정보 시트 생성
     * @param dispatchSeq
     * @param excelHandler
     */
    private void createExcelSheetForVehicleBasis(Long dispatchSeq, ExcelHandler excelHandler) {
        Sheet sheet = excelHandler.createSheet("차량기준정보");
        String[] headerNames = {"차량명/기사", "차량ID"};
        excelHandler.createHeaderCell(sheet, 0, 0, headerNames);

        //배차정보 조회
        DispatchDetailResult detailResult = dispatchMapper.findDetailById(dispatchSeq);
        if (detailResult == null) {
            throw new NotFoundException(ResponseCode.NOT_FOUND_DISPATCH);
        }

        //해당 센터의 차량목록 조회
        List<BelongingVehicleResult> vehicles = vehicleMapper.findByCenterSeq(detailResult.getCenterSeq());

        //차량별로 배송정보를 매핑
        List<Object[]> dataList = new ArrayList<>();
        for (BelongingVehicleResult vehicle : vehicles) {
            if (vehicle.getDriverSeq() == null) {
                continue;
            }

            Long vehicleSeq = vehicle.getVehicleSeq();
            String vehicleName = vehicle.getVehicleName() + " (" + vehicle.getDriverName() + ")";
            dataList.add(new Object[]{vehicleName, vehicleSeq});
        }
        excelHandler.createDataCell(sheet, 1, 0, dataList);
    }


    @Override
    public void readAndSaveDeliveryExcel(Long dispatchSeq, MultipartFile file) {

        log.debug("file name : {}", file.getName());
        log.debug("file size : {}", file.getSize());

        List<DeliveryForExcel> deliveryForExcels = null;
        try {
            try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
                Sheet sheet = workbook.getSheetAt(0);
                DeliveryExcelParser parser = new DeliveryExcelParser(sheet);
                deliveryForExcels = parser.parse();
                log.info("deliveryForExcels : {}", deliveryForExcels);
            }
        } catch (IOException e) {
            throw new BusinessException(ResponseCode.EXCEL_PARSE_FAILED);
        }

        int rowNumber = 1;

        List<ExcelError> errors = new ArrayList<>();
        for (DeliveryForExcel deliveryForExcel : deliveryForExcels) {
            Delivery delivery = dispatchConverter.toDelivery(deliveryForExcel);
            delivery.setDispatchSeq(dispatchSeq);

            //위도,경도 구하기
            try {
                GetCoordinateReqDto getCoordinateReqDto = new GetCoordinateReqDto();
                getCoordinateReqDto.setAddress(delivery.getDeliveryAddr1());
                GetCoordinateResDto coordinate = commonService.getCoordinate(getCoordinateReqDto);
                if (coordinate == null) {
                    throw new RuntimeException();
                }

                delivery.setLatitude(new BigDecimal(coordinate.getLatitude()));
                delivery.setLongitude(new BigDecimal(coordinate.getLongitude()));
            } catch (Exception e) {
                errors.add(new ExcelError(rowNumber, null, "배송지 주소(도로명/지번)", delivery.getDeliveryAddr1()
                        , "배송지 주소로 위도/경도 조회에 실패 하였습니다. 올바른 주소를 입력해 주세요"));
                rowNumber++;
                continue;
            }

            //배송정보 Insert
            try {
                this.createDelivery(delivery);
            } catch (InvalidRequestException e) {
                errors.add(new ExcelError(rowNumber, null, null, null, e.getMessage()));
            }
            rowNumber++;
        }

        if (!errors.isEmpty()) {
            log.error("Excel data createDelivery errors: {}", errors);
            throw new ExcelUploadException(errors);
        }

    }


    /**
     * 실제 Delivery 정보를 엑셀로 변환하여 반환
     * @param dispatchSeq
     * @return
     */
    @Override
    public CreateExcelFileForDeliveryResDto createExcelFileForDelivery(Long dispatchSeq) {
        // 실제 Delivery 목록 조회
        List<DeliveryResult> deliveryResults = deliveryMapper.findByDispatchSeq(dispatchSeq);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        List<Object[]> dataList = deliveryResults.stream()
                .map(delivery -> new Object[]{
                        delivery.getTrackingNum(), delivery.getRecipientName(), delivery.getRecipientPhoneNum(),
                        delivery.getDeliveryPostalCode(), delivery.getDeliveryAddr1(), delivery.getDeliveryAddr2(),
                        delivery.getVehicleSeq(), delivery.getProductName(), delivery.getBoxCount(),
                        delivery.getDeliveryTypeCdName(), delivery.getMemo(), delivery.getSecurityCode(),
                        delivery.getPickupDatetime() == null ? "" : delivery.getPickupDatetime().format(dateTimeFormatter),
                        delivery.getDeliveryEstimatedStartTime() == null ? "" : delivery.getDeliveryEstimatedStartTime().format(timeFormatter),
                        delivery.getDeliveryEstimatedEndTime() == null ? "" : delivery.getDeliveryEstimatedEndTime().format(timeFormatter)
                })
                .toList();

        ExcelHandler excelHandler = new ExcelHandler();
        this.createExcelSheetForDelivery(excelHandler, dataList);

        // 차량기준정보 시트 생성
        this.createExcelSheetForVehicleBasis(dispatchSeq, excelHandler);

        Dispatch dispatch = dispatchMapper.findById(dispatchSeq)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_DISPATCH));
        String fileName = dispatch.getDispatchName() + "_" + dispatch.getDeliveryDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".xlsx";
        log.info("Excel file name: {}", fileName);

        return CreateExcelFileForDeliveryResDto.builder()
                .workbook(excelHandler.getWorkbook())
                .fileName(fileName)
                .build();

    }
}
