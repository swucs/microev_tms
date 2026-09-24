# 티엠에스 API 명세

- 기준: 컨트롤러 코드 기준 정리. 상세 스키마는 각 모듈 스웨거 참조.
- 공통 응답 포맷: `ResponseDto{resultCode, resultMessage, data}`. 엑셀 다운로드 3건과 SSE 구독 1건은 예외(아래 별도 표기).
- 인증: JWT 베어러 방식. 요청 헤더 `Authorization: Bearer <accessToken>`.

## tms-admin (웹 관리자용)

- 로컬: `http://localhost:8090/tms/admin` / 포트 8090, 컨텍스트패스 `/tms/admin`.
- 로그인: `POST /login` (필터 처리, `LoginReqDto{email, password}` → `LoginResDto{portalUserSeq, email, adminName, accessToken, refreshToken}`).
- 토큰 재발급: `PUT /login/newToken` (`ReissueTokenReqDto{refreshToken, email}` → `ReissueTokenResDto{accessToken, refreshToken}`).

### 로그인 관련 (`/login`)

| 메서드 | 경로 | 요약 | 요청 | 응답 |
|---|---|---|---|---|
| POST | `/login` | 관리자 로그인 | `LoginReqDto{email, password}` (바디) | `LoginResDto` (토큰 포함) |
| PUT | `/login/newToken` | 신규 토큰 발급 | `ReissueTokenReqDto{refreshToken, email}` (바디) | `ReissueTokenResDto{accessToken, refreshToken}` |

### 관리자 관리 (`/admin`)

| 메서드 | 경로 | 요약 | 요청 | 응답 |
|---|---|---|---|---|
| GET | `/admin` | 관리자 목록 조회 | `SearchAdminsReqDto{email, adminName, statusCd}` (쿼리) | `List<SearchAdminsResDto{adminSeq, email, adminName, contact, position, lastAccessedAt, statusCd, statusCdName}>` |
| POST | `/admin` | 관리자 등록 | `CreateAdminReqDto{email, password, adminName, contact, position}` (바디) | `Long adminSeq` |
| PUT | `/admin/{adminSeq}` | 관리자 수정 | `ModifyAdminReqDto{password, adminName, contact, position, statusCd}` (바디) | 없음 |

### 센터관리 (`/center`)

| 메서드 | 경로 | 요약 | 요청 | 응답 |
|---|---|---|---|---|
| GET | `/center` | 센터 목록 조회 | `SearchCentersReqDto{centerName, centerTypeCd, managerName}` (쿼리) | `List<SearchCentersResDto{centerSeq, centerName, centerTypeCd(+Name), zipCode, centerAddr1, centerAddr2, managerName, contactNum, usageYn, vehicleCount}>` |
| POST | `/center` | 센터 등록 | `CreateCenterReqDto{centerName, centerTypeCd, zipCode, centerAddr1, centerAddr2, latitude, longitude, totalArea, managerName, contactNum, usageYn}` (바디) | `Long centerSeq` |
| GET | `/center/{centerSeq}` | 센터 상세 조회 | 경로 변수 | `CenterDetailResDto` (등록 필드 + `centerTypeCdName`) |
| PUT | `/center/{centerSeq}` | 센터 수정 | `ModifyCenterReqDto` (등록 필드와 동일 구성, 바디) | 없음 |
| DELETE | `/center/{centerSeq}` | 센터 삭제 | 경로 변수 | 없음 |
| GET | `/center/{centerSeq}/vehicle` | 센터 소속 차량운전자 조회 | 경로 변수 | `List<GetVehicleDriverResDto{vehicleSeq, vehicleName, driverSeq, driverName, vehicleNum}>` |

### 공통 (`/common`, 카카오 외부 연동)

| 메서드 | 경로 | 요약 | 요청 | 응답 |
|---|---|---|---|---|
| GET | `/common/coordinate` | 주소로 좌표 구하기 | `GetCoordinateReqDto{address}` (쿼리) | `GetCoordinateResDto{latitude, longitude}` |
| GET | `/common/eupMyeonDong` | 주소로 읍면동 조회 | `GetEupMyeonDongReqDto{address}` (쿼리) | `GetEupMyeonDongResDto{eupMyeonDong}` |

### 공통코드 관리 (`/common-code`)

| 메서드 | 경로 | 요약 | 요청 | 응답 |
|---|---|---|---|---|
| GET | `/common-code/valid-common-codes/{comCodeGroupCd}` | 유효 공통코드 목록 조회 | 경로 변수 | `List<GetValidCommonCodesResDto{comCodeGroupCd, comCodeCd, comCodeName, sortOrder}>` |
| GET | `/common-code/common-code-groups` | 그룹코드 목록 조회 | `SearchCommonCodeGroupsReqDto{comCodeGroupCd, comCodeGroupName}` (쿼리) | `List<SearchCommonCodeGroupsResDto{comCodeGroupCd, comCodeGroupName}>` |
| POST | `/common-code/common-code-group` | 그룹코드 생성 | `CreateCommonCodeGroupReqDto{comCodeGroupCd, comCodeGroupName}` (바디) | `CreateCommonCodeGroupResDto{comCodeGroupCd}` |
| PUT | `/common-code/common-code-group` | 그룹코드 수정 | `ModifyCommonCodeGroupReqDto{comCodeGroupCd, comCodeGroupName}` (바디) | `ModifyCommonCodeGroupResDto` (동일 구성) |
| DELETE | `/common-code/common-code-group` | 그룹코드 삭제 | `DeleteCommonCodeGroupReqDto{comCodeGroupCds}` (바디) | 없음 |
| GET | `/common-code/common-codes/{comCodeGroupCd}` | 공통코드 목록 조회 | 경로 변수 | `List<GetCommonCodesResDto{comCodeSeq, comCodeGroupCd, comCodeCd, comCodeName, sortOrder, usageYn, attribute1~3, createdAt, creatorSeq, modifiedAt}>` |
| POST | `/common-code/common-code` | 공통코드 생성 | `CreateCommonCodeReqDto{comCodeGroupCd, comCodeCd, comCodeName, sortOrder, usageYn, attribute1~3}` (바디) | `Long comCodeSeq` |
| PUT | `/common-code/common-code/{comCodeSeq}` | 공통코드 수정 | `ModifyCommonCodeReqDto{comCodeCd, comCodeName, sortOrder, usageYn, attribute1~3}` (바디) | 없음 |
| DELETE | `/common-code/common-code` | 공통코드 삭제 | `DeleteCommonCodeReqDto{comCodeSeqs}` (바디) | 없음 |

### 배차계획 (`/dispatch`)

| 메서드 | 경로 | 요약 | 요청 | 응답 |
|---|---|---|---|---|
| GET | `/dispatch` | 배차현황 목록 조회 | `SearchDispatchesReqDto{centerName, dispatchStatusCd}` (쿼리) | `List<SearchDispatchesResDto{dispatchSeq, dispatchName, deliveryDate, centerSeq, centerName, dispatchStatusCd(+Name), deliveryCount, deliveryVehicleCount}>` |
| POST | `/dispatch` | 배차생성 | `CreateDispatchReqDto{centerSeq, dispatchName, deliveryDate}` (바디) | `Long dispatchSeq` |
| PUT | `/dispatch/{dispatchSeq}` | 배차수정 | `ModifyDispatchReqDto{dispatchName, deliveryDate}` (바디) | `Long` (현재 null 반환) |
| GET | `/dispatch/{dispatchSeq}` | 배차상세 | 경로 변수 | `DispatchDetailResDto{dispatchSeq, centerSeq, centerName, dispatchName, dispatchStatusCd(+Name), deliveryDate, deliveriesByVehicle[]}` |
| PUT | `/dispatch/{dispatchSeq}/dispatched` | 배차확정(배송순서 저장) | `ModifyStatusToDispatchedReqDto{deliveryOrders[], vehicleSeq, deliverySeqList}` (바디) | 없음 |
| PUT | `/dispatch/{dispatchSeq}/waiting` | 배차대기로 변경 | 경로 변수 | 없음 |
| DELETE | `/dispatch/{dispatchSeq}` | 배차정보 삭제 | 경로 변수 | 없음 |
| POST | `/dispatch/autoDispatch` | 자동 차량배정(주소→권역→차량) | `AutoDispatchReqDto{centerSeq, roadAddress}` (바디) | `AutoDispatchResDto{regionSeq, regionName, vehicleSeq, vehicleName}` |
| POST | `/dispatch/delivery` | 배송정보 추가 | `CreateDeliveryReqDto{dispatchSeq, regionSeq, trackingNum, recipientName, recipientPhoneNum, deliveryPostalCode, deliveryAddr1, deliveryAddr2, productName, latitude, longitude, boxCount}` (바디) | `Long deliverySeq` |
| PUT | `/dispatch/delivery/{deliverySeq}` | 배송정보 수정 | `ModifyDeliveryReqDto` (추가 필드와 동일 + `memo`, 바디) | 없음 |
| DELETE | `/dispatch/delivery/{deliverySeq}` | 배송정보 삭제 | 경로 변수 | 없음 |
| GET | `/dispatch/delivery/excel/download-sample-delivery/{dispatchSeq}` | 배송정보 샘플 엑셀 다운로드 | 경로 변수 | 엑셀 바이너리 스트림 (`ResponseDto` 아님) |
| POST | `/dispatch/delivery/excel/upload/{dispatchSeq}` | 배송정보 엑셀 업로드 | `multipart file` (폼) | 없음 |
| GET | `/dispatch/delivery/excel/download/{dispatchSeq}` | 실제 배송정보 엑셀 다운로드 | 경로 변수 | 엑셀 바이너리 스트림 (`ResponseDto` 아님) |

### 기사관리 (`/driver`)

| 메서드 | 경로 | 요약 | 요청 | 응답 |
|---|---|---|---|---|
| GET | `/driver` | 배송기사 목록 조회 | `SearchDriversReqDto{driverName, centerName, statusCd}` (쿼리) | `List<SearchDriversResDto{driverSeq, driverName, centerSeq, centerName, loginId, extSystemLinkedId, driverPhoneNum, email, driverLicenseNum(+Date, +Agency), compName, ...}>` |
| POST | `/driver` | 배송기사 등록 | `CreateDriverReqDto{driverName, centerSeq, loginId, password, extSystemLinkedId, driverPhoneNum, email, driverLicenseNum(+Date, +Agency), compName, workingDays}` (바디) | `Long driverSeq` |
| PUT | `/driver/{driverSeq}` | 배송기사 수정 | `ModifyDriverReqDto` (등록 필드에서 비밀번호 제외 + `workingStartHour, workingEndHour, statusCd`, 바디) | 없음 |
| PUT | `/driver/{driverSeq}/password` | 비밀번호 수정 | `ModifyDriverPasswordReqDto{password}` (바디) | 없음 |
| DELETE | `/driver/{driverSeq}` | 배송기사 삭제 | 경로 변수 | 없음 |

### 권역관리 (`/region`)

| 메서드 | 경로 | 요약 | 요청 | 응답 |
|---|---|---|---|---|
| GET | `/region` | 권역 조회 | `SearchRegionsReqDto{regionName, vehicleName, eupMyeonDong}` (쿼리) | `List<SearchRegionsResDto{regionSeq, regionName, eupMyeonDong, vehicleName, creatorName, createdAt}>` |
| POST | `/region` | 권역 등록 | `CreateRegionReqDto{regionName, eupMyeonDongs[]}` (바디) | `Long regionSeq` |
| GET | `/region/{regionSeq}` | 권역 상세 조회 | 경로 변수 | `RegionDetailResDto{regionSeq, regionName, eupMyeonDongs[], belongingVehicles[]}` |
| PUT | `/region/{regionSeq}` | 권역 수정 | `ModifyRegionReqDto{regionName, eupMyeonDongs[]}` (바디) | 없음 |
| DELETE | `/region/{regionSeq}` | 권역 삭제 | 경로 변수 | 없음 |

### 차량관리 (`/vehicle`)

| 메서드 | 경로 | 요약 | 요청 | 응답 |
|---|---|---|---|---|
| GET | `/vehicle` | 차량 목록 조회 | `SearchVehiclesReqDto{vehicleName, centerName, vehicleTypeCd}` (쿼리) | `List<SearchVehiclesResDto{vehicleSeq, centerSeq, centerName, regionSeq, regionName, driverSeq, driverName, vehicleName, model, vehicleTypeCd(+Name), vehicleNum}>` |
| POST | `/vehicle` | 차량 등록 | `CreateVehicleReqDto{vehicleName, model, centerSeq, driverSeq, regionSeq, vehicleTypeCd, vehicleNum, vehicleRegNum, modelYear, vehicleRegArea, ownerName, length, ...}` (바디) | `Long vehicleSeq` |
| PUT | `/vehicle/{vehicleSeq}` | 차량 수정 | `ModifyVehicleReqDto` (등록과 동일 구성, 바디) | 없음 |
| DELETE | `/vehicle/{vehicleSeq}` | 차량 삭제 | 경로 변수 | 없음 |

## tms-api (기사 앱용)

- 로컬: `http://localhost:8091/tms/api` / 포트 8091, 컨텍스트패스 `/tms/api`.
- 인증: `JwtTokenInterceptor`가 전체 경로에 적용되며 `/auth/login`·스웨거·정적 리소스만 제외. 그 외 전부 토큰 필요.
- 실시간 알림: 엠큐티 없이 `tms-api` 내부 SSE 직접 발행. `GET /delivery/subscribe` 구독 후 배송·검수 상태 변경 시 `deliveryEvent` 수신.

### 인증관련 (`/auth`)

| 메서드 | 경로 | 요약 | 요청 | 응답 |
|---|---|---|---|---|
| POST | `/auth/login` | 차량 로그인(토큰 발급, 인증 불필요) | `DriverLoginReqDto{loginId, password}` (바디) | `DriverLoginResDto{driverSeq, driverName, vehicleSeq, vehicleName, loginId, email, accessToken, refreshToken}` |
| POST | `/auth/logout` | 차량 로그아웃(토큰 삭제) | 없음 (토큰 필요) | 없음 |
| GET | `/auth/mobile-token` | 모바일용 토큰 반환 | 없음 (토큰 필요) | `DriverLoginResDto` (동일 구성) |

### 배송관련 (`/delivery`)

| 메서드 | 경로 | 요약 | 요청 | 응답 |
|---|---|---|---|---|
| GET | `/delivery/main` | 메인화면 정보 | 없음 (토큰 필요) | `GetMainResDto{dispatchSeq, driverDeliveryStatus, totalCount, deliveryTotalCount, deliveryTotalCompletedCount, deliveryCompletedCount, deliveryUncompletedCount, collectionTotalCount, collectionTotalCompletedCount, collectionCompletedCount, collectionUncompletedCount}` |
| GET | `/delivery/status` | 배송현황 | 없음 (토큰 필요) | `GetDeliveryStatusResDto` (메인 구성 + `deliverySummaries[]`) |
| GET | `/delivery/waybill` | 운송장 리스트 조회 | `GetWaybillsReqDto{address}` (쿼리) | `GetWaybillsResDto{address, totalBoxCount, deliveryBoxCount, collectionBoxCount, waybills[]{deliverySeq, trackingNum, productName, boxCount, memo, securityCode, deliveryAddr1, ...}}` |
| PUT | `/delivery/mobile/{deliverySeq}/completed` | 배송완료 처리 | `multipart`: `deliveryCompleted`(JSON `ProcessCompletedReqDto{shootingImpossibleYn, shootingImpossibleReasonCd, consignmentLocationCd}`) + `attachFile`(선택) | `ProcessCompletedResDto{isAllCompleted}` |
| PUT | `/delivery/mobile/{deliverySeq}/uncompleted` | 미배송·미수거 처리 | `ProcessUncompletedReqDto{uncompletedReasonCd}` (바디) | `ProcessUncompletedResDto{isAllCompleted}` |
| GET | `/delivery/subscribe` | 배송상태 변경 구독 (SSE) | 없음 (토큰 필요) | `text/event-stream`. 이벤트명 `deliveryEvent`, 데이터 `DeliveryChangedEvent{deliverySeq, driverSeq, deliveryStatusCd, eventDatetime}` JSON. 최초 연결 시 `EventStream Created` 1건 수신. 타임아웃 60분. |

### 상품검수관련 (`/inspection`)

| 메서드 | 경로 | 요약 | 요청 | 응답 |
|---|---|---|---|---|
| GET | `/inspection/mobile` | 상품검수 리스트 조회 | 없음 (토큰 필요) | `GetMobileInspectionResDto{totalBoxCount, inspectionCompletedCount, inspectionWaitingCount, inspections[]{deliverySeq, trackingNum, deliveryAddr1, deliveryAddr2, boxCount, productName, deliveryStatusCd(+Name)}}` |
| PUT | `/inspection/mobile/{deliverySeq}/completed` | 검수완료 처리 | 경로 변수 | 없음 |
| PUT | `/inspection/mobile/{deliverySeq}/cancel` | 검수취소 처리 | 경로 변수 | 없음 |

### 통계관련 (`/statistics`)

| 메서드 | 경로 | 요약 | 요청 | 응답 |
|---|---|---|---|---|
| GET | `/statistics/monthly` | 월별 배송 통계 | `GetMonthlyReqDto{yyyyMM}` (쿼리) | `GetMonthlyResDto{summary{totalCount, deliveryCompletedCount, deliveryUncompletedCount, collectionCompletedCount, collectionUncompletedCount, processRatio, workMinutes}, dailies[]{deliveryDate, totalCount, ...}}` |

## 수동 검증

- `http/admin/*.http`, `http/api/*.http`와 `http/http-client.env.json` 조합 사용. 로컬은 `localhost:8090`·`8091`, 운영은 `evcar-tms.obigo.com` 기준.
- SSE 구독(`/tms/api/delivery/subscribe`)은 `nginx/default.conf`에서 버퍼링 해제로 별도 처리됨. 일반 프록시 설정으로 검증하지 말 것.
