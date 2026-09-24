# 티엠에스 DB 스키마

## 1. 문서 목적

이 문서는 `backend/database/createTable.sql`을 기준으로 티엠에스(TMS)의 PostgreSQL 스키마를 정리한 문서입니다. 테이블, 컬럼, 키, 참조 관계와 초기 코드 데이터의 사용 방법을 한곳에서 확인하기 위한 참고 자료입니다.

- DBMS: PostgreSQL
- 스키마 정의: `database/createTable.sql`
- 초기 데이터: `database/initData.sql`
- 개발용 샘플 데이터: `database/testData.sql`
- 테이블 접두사: `t_`
- 시퀀스 기반 PK 접미사: `_seq`
- 테이블 수: 14개

> 애플리케이션의 실제 DB 접근은 MyBatis Mapper와 XML SQL을 사용한다. 이 문서는 스키마의 빠른 확인용이며, 운영 시에는 SQL 파일의 정의를 우선한다.

## 2. 초기화 및 운영 규칙

### 2.1 데이터 적용 순서

```text
createTable.sql
  → initData.sql
  → testData.sql (개발/검증 시 선택)
```

- `createTable.sql`: 테이블, 컬럼, 인덱스, 기본키, 유니크 제약, 외래키, 주석 정의
- `initData.sql`: 관리자 1행, 공통코드 그룹 12개, 공통코드 42개를 초기화
- `testData.sql`: 센터, 기사, 권역, 차량 중심의 개발용 샘플 데이터

### 2.2 공통 규칙

- `BIGSERIAL` 컬럼은 PostgreSQL 시퀀스를 사용하는 자동 증가 정수다.
- `NOT NULL` 컬럼은 애플리케이션 입력과 DB 제약 양쪽에서 필수다.
- `createTable.sql`에는 별도의 `DEFAULT` 또는 `CHECK` 제약이 정의되어 있지 않다.
- `creator_seq`, `modifier_seq`는 감사 필드이며 DB 외래키로 연결되어 있지 않다.
- `created_at`, `modified_at`은 애플리케이션에서 관리하는 감사 시각이며 DB 기본값이 없다.
- 모든 외래키는 별도의 `ON DELETE` 동작을 지정하지 않아 PostgreSQL 기본 동작인 `NO ACTION`으로 동작한다.
- FK 정의는 `createTable.sql` 하단(약 1212~1406행)에 모아져 있다.
- 모든 `TIMESTAMP`은 timezone 정보가 없는 `TIMESTAMP`이다.
- DDL은 명시적인 schema를 지정하지 않는다. `testData.sql`만 `public.t_*`를 명시하므로 실행 환경의 `search_path`를 확인해야 한다.

### 2.3 제약·인덱스 요약

| 구분 | 개수/내용 |
|---|---|
| PK | 14개 |
| UK | 6개 |
| FK | 14개 |
| `CREATE UNIQUE INDEX` | 20개 (PK backing index 14개 + UK backing index 6개) |
| 일반 `CREATE INDEX` | 1개 (`IX_t_delivery`) |

- 모든 인덱스 컬럼은 `ASC NULLS LAST`로 정의되어 있다.
- 모든 PK와 UK는 `NOT DEFERRABLE`이다.
- 별도의 trigger, generated column, partial index는 없다.
- `createTable.sql`은 `DEFAULT`, `CHECK`, `ON CONFLICT`, 초기화용 `DROP`/`TRUNCATE`를 포함하지 않는다.

### 2.4 공통 감사 컬럼

대부분의 업무·기준 테이블에 다음 컬럼이 있다.

| 컬럼 | 타입 | NULL | 설명 |
|---|---|---:|---|
| `created_at` | `TIMESTAMP` | 아니오 | 등록일시 |
| `creator_seq` | `BIGINT` | 아니오 | 등록자 시퀀스 |
| `modified_at` | `TIMESTAMP` | 아니오 | 수정일시 |
| `modifier_seq` | `BIGINT` | 아니오 | 수정자 시퀀스 |

예외적으로 `t_delivery_history`와 `t_attach`는 `created_at`, `creator_seq`만 가지고, `t_api_call`은 `created_at`만 가진다. `t_file`은 감사 컬럼이 없다.

## 3. 전체 관계 요약

```text
t_center
 ├─< t_driver
 ├─< t_vehicle
 └─< t_dispatch ──< t_delivery ──< t_delivery_history
                         │
                         ├─> t_region
                         ├─> t_vehicle
                         ├─> t_driver
                         └─> t_attach ──< t_file

t_region ──< t_region_range
t_common_code_group ──< t_common_code
```

### 3.1 주요 참조 관계

| 자식 테이블 | 부모 테이블 | 컬럼 | nullable | 비고 |
|---|---|---|---:|---|
| `t_driver` | `t_center` | `center_seq` | 아니오 | 기사는 센터 종속 |
| `t_vehicle` | `t_center` | `center_seq` | 아니오 | 차량은 센터 종속 |
| `t_vehicle` | `t_region` | `region_seq` | 예 | 배정 권역 |
| `t_vehicle` | `t_driver` | `driver_seq` | 예 | 배정 기사. `driver_seq`에 유니크 제약이 있어 NULL은 여러 행 가능 |
| `t_dispatch` | `t_center` | `center_seq` | 아니오 | 센터별 배송일자 배차 |
| `t_delivery` | `t_dispatch` | `dispatch_seq` | 아니오 | 배송은 배차에 반드시 귀속 |
| `t_delivery` | `t_vehicle` | `vehicle_seq` | 예 | 배차 확정 시 차량 배정 |
| `t_delivery` | `t_driver` | `driver_seq` | 예 | 배차 확정 시 기사 배정 |
| `t_delivery` | `t_region` | `region_seq` | 예 | 배송지 권역 |
| `t_delivery` | `t_attach` | `delivery_photo_attach_seq` | 예 | 배송사진 첨부 묶음 |
| `t_delivery_history` | `t_delivery` | `delivery_seq` | 아니오 | 배송 이력 |
| `t_region_range` | `t_region` | `region_seq` | 아니오 | 권역별 읍면동 범위 |
| `t_file` | `t_attach` | `attach_seq` | 아니오 | 첨부 묶음에 속한 파일 |
| `t_common_code` | `t_common_code_group` | `com_code_group_cd` | 아니오 | 공통코드 그룹 |

FK 제약명은 `FK_참조테이블_TO_자식테이블` 형식으로 정의되어 있다.

- `FK_t_center_TO_t_vehicle`
- `FK_t_region_TO_t_vehicle`
- `FK_t_driver_TO_t_vehicle`
- `FK_t_center_TO_t_driver`
- `FK_t_dispatch_TO_t_delivery`
- `FK_t_vehicle_TO_t_delivery`
- `FK_t_region_TO_t_delivery`
- `FK_t_driver_TO_t_delivery`
- `FK_t_attach_TO_t_delivery`
- `FK_t_center_TO_t_dispatch`
- `FK_t_common_code_group_TO_t_common_code`
- `FK_t_region_TO_t_region_range`
- `FK_t_delivery_TO_t_delivery_history`
- `FK_t_attach_TO_t_file`

`t_delivery_history.dispatch_seq`, `t_delivery_history.driver_seq`, `t_api_call.driver_seq`는 값은 보관하지만 SQL FK로 연결되어 있지 않다. 이 값들은 애플리케이션 조회 시 다른 테이블과 조인하는 비정규화/기록용 컬럼으로 취급한다.

### 3.2 설계상 주의점

- `t_dispatch`에는 센터와 배송일자만 있고, 차량·기사·권역은 개별 `t_delivery` 행에 선택적으로 저장된다. 따라서 한 배차의 배송들이 서로 다른 차량·기사·권역을 가질 수 있다.
- `t_delivery`의 `dispatch_seq`만 NOT NULL FK이고, `vehicle_seq`·`driver_seq`·`region_seq`는 nullable이다.
- `t_vehicle.driver_seq`의 UK 때문에 한 기사는 최대 한 차량에만 연결된다. 단, NULL 차량 배정은 여러 대 가능하다.
- DB는 차량과 배정 기사가 같은 센터인지, 배차와 배송의 센터가 일치하는지 강제하지 않는다.
- `t_delivery.delivery_photo_attach_seq`에는 UK가 없으므로 하나의 첨부가 여러 배송에서 재사용되는 것도 DB가 막지 않는다.
- 모든 상태/유형 코드는 `t_common_code`와 문자열로만 연결되고, 코드 삭제·변경에 따른 업무 데이터 보호는 DB에서 제공되지 않는다.
- 상태 전이, 완료일시와 상태의 조합, 사유 코드와 상태의 조합을 강제하는 trigger/CHECK는 없다.

## 4. 테이블별 스키마

아래 표의 `PK`는 기본키, `UK`는 유니크 제약, `FK`는 외래키다. 설명은 `createTable.sql`의 컬럼 주석을 기준으로 정리했다.

### 4.1 `t_center` — 센터

**목적:** 배송 거점의 이름, 주소, 좌표, 담당자 정보를 저장한다. 기사와 차량의 기준 데이터가 된다.

| 컬럼 | 타입 | NULL | 키/비고 | 설명 |
|---|---|---:|---|---|
| `center_seq` | `BIGSERIAL` | 아니오 | PK | 센터 시퀀스 |
| `center_name` | `VARCHAR(100)` | 아니오 |  | 센터명 |
| `center_type_cd` | `VARCHAR(50)` | 아니오 | 코드 | 센터 유형 코드 |
| `center_addr_1` | `VARCHAR(100)` | 아니오 |  | 주소 |
| `center_addr_2` | `VARCHAR(100)` | 아니오 |  | 상세 주소 |
| `zip_code` | `VARCHAR(10)` | 아니오 |  | 우편번호 |
| `latitude` | `NUMERIC(10,7)` | 아니오 |  | 위도 |
| `longitude` | `NUMERIC(10,7)` | 아니오 |  | 경도 |
| `total_area` | `NUMERIC` | 예 |  | 총면적 |
| `manager_name` | `VARCHAR(30)` | 아니오 |  | 담당자 |
| `contact_num` | `VARCHAR(20)` | 아니오 |  | 연락처 |
| `usage_yn` | `CHAR(1)` | 아니오 |  | 사용 여부 |
| `created_at` | `TIMESTAMP` | 아니오 |  | 등록일시 |
| `creator_seq` | `BIGINT` | 아니오 |  | 등록자 |
| `modified_at` | `TIMESTAMP` | 아니오 |  | 수정일시 |
| `modifier_seq` | `BIGINT` | 아니오 |  | 수정자 |

**키/인덱스**

- `t_center_info_pk`: `center_seq` PK
- 별도 유니크 제약 없음

### 4.2 `t_region` — 권역

**목적:** 자동 배차에서 주소와 차량을 연결하는 배송 권역의 기준 정보다.

| 컬럼 | 타입 | NULL | 키/비고 | 설명 |
|---|---|---:|---|---|
| `region_seq` | `BIGSERIAL` | 아니오 | PK | 권역 시퀀스 |
| `region_name` | `VARCHAR(100)` | 아니오 |  | 권역명 |
| `created_at` | `TIMESTAMP` | 아니오 |  | 등록일시 |
| `creator_seq` | `BIGINT` | 아니오 |  | 등록자 |
| `modified_at` | `TIMESTAMP` | 아니오 |  | 수정일시 |
| `modifier_seq` | `BIGINT` | 아니오 |  | 수정자 |

**키/인덱스**

- `t_region_info_pk`: `region_seq` PK
- 별도 유니크 제약 없음

### 4.3 `t_region_range` — 권역 범위

**목적:** 권역 하나에 포함되는 읍면동 문자열을 저장한다. 주소 검색 결과를 권역에 매핑할 때 사용한다.

| 컬럼 | 타입 | NULL | 키/비고 | 설명 |
|---|---|---:|---|---|
| `region_seq` | `BIGINT` | 아니오 | PK, FK → `t_region.region_seq` | 권역 시퀀스 |
| `eup_myeon_dong` | `VARCHAR(100)` | 아니오 | PK | 읍면동 |

**키/인덱스**

- `PK_t_region_range`: `(region_seq, eup_myeon_dong)` 복합 PK
- 하나의 권역에 여러 읍면동 범위를 저장할 수 있다.

### 4.4 `t_driver` — 배송기사

**목적:** 기사 정보와 기사 로그인 계정 정보를 저장한다. 모든 기사는 하나의 센터에 종속된다.

| 컬럼 | 타입 | NULL | 키/비고 | 설명 |
|---|---|---:|---|---|
| `driver_seq` | `BIGSERIAL` | 아니오 | PK | 기사 시퀀스 |
| `driver_name` | `VARCHAR(30)` | 아니오 |  | 기사명 |
| `center_seq` | `BIGINT` | 아니오 | FK → `t_center.center_seq` | 소속 센터 |
| `login_id` | `VARCHAR(50)` | 아니오 | UK | 로그인 아이디 |
| `password` | `VARCHAR(100)` | 아니오 |  | 암호화된 비밀번호 |
| `ext_system_linked_id` | `VARCHAR(50)` | 아니오 | UK | 외부 시스템 연동 아이디 |
| `driver_phone_num` | `VARCHAR(20)` | 아니오 |  | 연락처 |
| `email` | `VARCHAR(50)` | 아니오 |  | 이메일 |
| `driver_license_num` | `VARCHAR(15)` | 아니오 |  | 운전면허 번호 |
| `driver_license_date` | `DATE` | 아니오 |  | 운전면허 발급일 |
| `driver_license_agency` | `VARCHAR(20)` | 아니오 |  | 운전면허 발급기관 |
| `working_days` | `VARCHAR(20)` | 예 |  | 근무요일 |
| `working_start_hour` | `TIME` | 예 |  | 근무 시작시간 |
| `working_end_hour` | `TIME` | 예 |  | 근무 종료시간 |
| `status_cd` | `VARCHAR(50)` | 아니오 | 코드 | 기사 상태 |
| `comp_name` | `VARCHAR(100)` | 예 |  | 소속 업체명 |
| `created_at` | `TIMESTAMP` | 아니오 |  | 등록일시 |
| `creator_seq` | `BIGINT` | 아니오 |  | 등록자 |
| `modified_at` | `TIMESTAMP` | 아니오 |  | 수정일시 |
| `modifier_seq` | `BIGINT` | 아니오 |  | 수정자 |

**키/인덱스**

- `t_driver_info_pk`: `driver_seq` PK
- `UK_t_driver`: `login_id` 유니크
- `UK_t_driver2`: `ext_system_linked_id` 유니크

### 4.5 `t_vehicle` — 차량

**목적:** 차량의 기본 정보와 소속 센터·배정 권역·배정 기사를 관리한다. 기사에는 최대 한 대의 차량을 배정할 수 있도록 설계되어 있다.

| 컬럼 | 타입 | NULL | 키/비고 | 설명 |
|---|---|---:|---|---|
| `vehicle_seq` | `BIGSERIAL` | 아니오 | PK | 차량 시퀀스 |
| `center_seq` | `BIGINT` | 아니오 | FK → `t_center.center_seq` | 소속 센터 |
| `driver_seq` | `BIGINT` | 예 | FK → `t_driver.driver_seq`, UK | 배정 기사 |
| `region_seq` | `BIGINT` | 예 | FK → `t_region.region_seq` | 배정 권역 |
| `vehicle_name` | `VARCHAR(100)` | 아니오 |  | 차량명 |
| `model` | `VARCHAR(50)` | 아니오 |  | 차종 |
| `vehicle_type_cd` | `VARCHAR(50)` | 아니오 | 코드 | 차량 유형 코드 |
| `vehicle_num` | `VARCHAR(10)` | 아니오 |  | 차량번호 |
| `vehicle_reg_num` | `VARCHAR(10)` | 아니오 |  | 차량등록번호 |
| `model_year` | `VARCHAR(4)` | 아니오 |  | 연식 |
| `vehicle_reg_area` | `VARCHAR(50)` | 예 |  | 차량 등록지 |
| `owner_name` | `VARCHAR(30)` | 아니오 |  | 차량 소유자명 |
| `length` | `VARCHAR(20)` | 아니오 |  | 길이 |
| `width` | `VARCHAR(20)` | 아니오 |  | 넓이 |
| `ton_grade` | `VARCHAR(20)` | 아니오 |  | 톤급 |
| `max_loading_capacity` | `VARCHAR(20)` | 아니오 |  | 최대 적재량 |
| `fuel_type_cd` | `VARCHAR(50)` | 아니오 | 코드 | 연료 구분 코드 |
| `fuel_efficiency` | `VARCHAR(20)` | 아니오 |  | 연비 |
| `vehicle_use_type_cd` | `VARCHAR(50)` | 아니오 | 코드 | 자용 구분 코드 |
| `garage_name` | `VARCHAR(100)` | 아니오 |  | 차고지명 |
| `usage_yn` | `CHAR(1)` | 아니오 |  | 사용 여부 |
| `created_at` | `TIMESTAMP` | 아니오 |  | 등록일시 |
| `creator_seq` | `BIGINT` | 아니오 |  | 등록자 |
| `modified_at` | `TIMESTAMP` | 아니오 |  | 수정일시 |
| `modifier_seq` | `BIGINT` | 아니오 |  | 수정자 |

**키/인덱스**

- `t_vehicle_info_pk`: `vehicle_seq` PK
- `UK_t_vehicle`: `driver_seq` 유니크
- PostgreSQL에서는 UNIQUE 컬럼에 여러 개의 `NULL`을 저장할 수 있으므로, 미배정 차량을 여러 대 둘 수 있다.

### 4.6 `t_dispatch` — 배차

**목적:** 센터와 배송일자를 기준으로 한 배차 단위를 관리한다. 배차 상태는 `DispatchStatus` 공통코드로 관리한다.

| 컬럼 | 타입 | NULL | 키/비고 | 설명 |
|---|---|---:|---|---|
| `dispatch_seq` | `BIGSERIAL` | 아니오 | PK | 배차 시퀀스 |
| `center_seq` | `BIGINT` | 아니오 | FK → `t_center.center_seq` | 소속 센터 |
| `dispatch_name` | `VARCHAR(100)` | 아니오 |  | 배차명 |
| `delivery_date` | `DATE` | 아니오 |  | 배송일자 |
| `dispatch_status_cd` | `VARCHAR(50)` | 아니오 | 코드 | 배차 상태 코드 |
| `created_at` | `TIMESTAMP` | 아니오 |  | 등록일시 |
| `creator_seq` | `BIGINT` | 아니오 |  | 등록자 |
| `modified_at` | `TIMESTAMP` | 아니오 |  | 수정일시 |
| `modifier_seq` | `BIGINT` | 아니오 |  | 수정자 |

**키/인덱스**

- `t_dispatch_pk`: `dispatch_seq` PK
- 별도 유니크 제약 없음

### 4.7 `t_delivery` — 배송

**목적:** 배차에 포함된 개별 배송·수거 데이터다. 현재 상태와 처리 결과(완료/미완료/사진/위탁 위치)를 함께 보관한다.

| 컬럼 | 타입 | NULL | 키/비고 | 설명 |
|---|---|---:|---|---|
| `delivery_seq` | `BIGSERIAL` | 아니오 | PK | 배송 시퀀스 |
| `dispatch_seq` | `BIGINT` | 아니오 | FK → `t_dispatch.dispatch_seq` | 배차 시퀀스 |
| `vehicle_seq` | `BIGINT` | 예 | FK → `t_vehicle.vehicle_seq` | 배정 차량 |
| `driver_seq` | `BIGINT` | 예 | FK → `t_driver.driver_seq` | 배정 기사 |
| `region_seq` | `BIGINT` | 예 | FK → `t_region.region_seq` | 배송 권역 |
| `delivery_order` | `SMALLINT` | 아니오 |  | 배송 순서 |
| `tracking_num` | `VARCHAR(50)` | 아니오 | UK | 운송장/배송 번호 |
| `recipient_name` | `VARCHAR(30)` | 아니오 |  | 수령인 이름 |
| `recipient_phone_num` | `VARCHAR(20)` | 아니오 |  | 수령인 연락처 |
| `delivery_postal_code` | `VARCHAR(10)` | 아니오 |  | 배송지 우편번호 |
| `delivery_addr_1` | `VARCHAR(100)` | 아니오 |  | 배송지 주소 |
| `delivery_addr_2` | `VARCHAR(100)` | 아니오 |  | 배송지 상세 주소 |
| `delivery_type_cd` | `VARCHAR(50)` | 아니오 | 코드 | 배송/수거 유형 |
| `product_name` | `VARCHAR(100)` | 아니오 |  | 상품명 |
| `box_count` | `SMALLINT` | 아니오 |  | 박스 수량 |
| `memo` | `VARCHAR(1000)` | 예 |  | 배송 메모 |
| `security_code` | `VARCHAR(50)` | 예 |  | 출입번호 |
| `pickup_datetime` | `TIMESTAMP` | 예 |  | 집하일시(택배) |
| `latitude` | `NUMERIC(10,7)` | 아니오 |  | 위도 |
| `longitude` | `NUMERIC(10,7)` | 아니오 |  | 경도 |
| `delivery_status_cd` | `VARCHAR(50)` | 아니오 | 코드 | 배송 상태 코드 |
| `delivery_estimated_start_time` | `TIME` | 예 |  | 배송 예정 시작시간 |
| `delivery_estimated_end_time` | `TIME` | 예 |  | 배송 예정 종료시간 |
| `delivery_datetime` | `TIMESTAMP` | 예 |  | 배송 완료일시 |
| `shooting_impossible_yn` | `CHAR(1)` | 예 |  | 촬영 불가 여부 |
| `shooting_impossible_reason_cd` | `VARCHAR(50)` | 예 | 코드 | 촬영 불가 사유 |
| `consignment_location_cd` | `VARCHAR(50)` | 예 | 코드 | 위탁 장소 |
| `uncompleted_reason_cd` | `VARCHAR(50)` | 예 | 코드 | 미완료 사유 |
| `delivery_photo_attach_seq` | `BIGINT` | 예 | FK → `t_attach.attach_seq` | 배송사진 첨부 시퀀스 |
| `created_at` | `TIMESTAMP` | 아니오 |  | 등록일시 |
| `creator_seq` | `BIGINT` | 아니오 |  | 등록자 |
| `modified_at` | `TIMESTAMP` | 아니오 |  | 수정일시 |
| `modifier_seq` | `BIGINT` | 아니오 |  | 수정자 |

**키/인덱스**

- `t_delivery_pk`: `delivery_seq` PK
- `UK_t_delivery`: `tracking_num` 유니크
- `IX_t_delivery`: `(dispatch_seq, vehicle_seq)` 일반 인덱스
- `t_delivery_pk` 인덱스의 주석만 `t_order_info_pk`로 남아 있어, 실제 인덱스/제약 이름과 주석이 불일치한다.
- `dispatch_seq`는 NOT NULL이므로 모든 배송은 배차에 속해야 한다.
- `vehicle_seq`, `driver_seq`, `region_seq`는 배차 확정 전에는 NULL일 수 있다.
- `(dispatch_seq, delivery_order)`에는 UK가 없어 같은 배차에서 배송 순서가 중복될 수 있다.
- `box_count`, `delivery_order`의 양수 여부와 `delivery_status_cd`/사유 코드 조합은 DB에서 검증하지 않는다.

### 4.8 `t_delivery_history` — 배송 이력

**목적:** 배송 상태가 변경될 때의 상태와 처리자 정보를 기록한다. 현재 상태는 `t_delivery`에, 과거 상태는 이 테이블에 보관한다.

| 컬럼 | 타입 | NULL | 키/비고 | 설명 |
|---|---|---:|---|---|
| `delivery_history_seq` | `BIGSERIAL` | 아니오 | PK | 배송 이력 시퀀스 |
| `delivery_seq` | `BIGINT` | 아니오 | FK → `t_delivery.delivery_seq` | 배송 시퀀스 |
| `dispatch_seq` | `BIGINT` | 아니오 | 기록용, FK 없음 | 배차 시퀀스 |
| `driver_seq` | `BIGINT` | 아니오 | 기록용, FK 없음 | 기사 시퀀스 |
| `delivery_status_cd` | `VARCHAR(50)` | 아니오 | 코드 | 변경된 배송 상태 |
| `created_at` | `TIMESTAMP` | 아니오 |  | 이력 등록일시 |
| `creator_seq` | `BIGINT` | 아니오 |  | 등록자 |

**키/인덱스**

- `PK_t_delivery_history`: `delivery_history_seq` PK
- `dispatch_seq`와 `driver_seq`는 이력 조회 편의를 위한 비정규화 컬럼이며 DB FK 제약이 없다.

### 4.9 `t_attach` — 첨부 묶음

**목적:** 여러 파일을 하나의 첨부 단위로 묶고, 배송사진과 연결하기 위한 테이블이다.

| 컬럼 | 타입 | NULL | 키/비고 | 설명 |
|---|---|---:|---|---|
| `attach_seq` | `BIGSERIAL` | 아니오 | PK | 첨부 시퀀스 |
| `creator_seq` | `BIGINT` | 아니오 |  | 등록자 |
| `created_at` | `TIMESTAMP` | 아니오 |  | 등록일시 |

**키/인덱스**

- `PK_t_attach`: `attach_seq` PK

### 4.10 `t_file` — 파일

**목적:** 첨부 묶음에 속한 실제 파일의 저장 경로와 원본 파일명을 저장한다.

| 컬럼 | 타입 | NULL | 키/비고 | 설명 |
|---|---|---:|---|---|
| `file_seq` | `BIGSERIAL` | 아니오 | PK | 파일 시퀀스 |
| `attach_seq` | `BIGINT` | 아니오 | FK → `t_attach.attach_seq` | 첨부 시퀀스 |
| `file_full_path` | `VARCHAR(255)` | 아니오 |  | 파일 전체 경로 |
| `original_file_name` | `VARCHAR(100)` | 아니오 |  | 원본 파일명 |
| `sort_order` | `SMALLINT` | 아니오 |  | 정렬 순서 |

**키/인덱스**

- `PK_t_file`: `file_seq` PK
- 하나의 `t_attach`에 여러 파일을 연결할 수 있다.

### 4.11 `t_api_call` — 외부 API 호출 기록

**목적:** 카카오 주소 API 등 외부 API의 호출 시간과 응답 결과를 기록한다. 현재 API 호출 로그 저장소로 사용된다.

| 컬럼 | 타입 | NULL | 키/비고 | 설명 |
|---|---|---:|---|---|
| `api_call_seq` | `BIGSERIAL` | 아니오 | PK | API 호출 시퀀스 |
| `api_url` | `VARCHAR(255)` | 아니오 |  | API URL |
| `http_method` | `VARCHAR(20)` | 아니오 |  | HTTP 메서드 |
| `start_time` | `TIMESTAMP` | 아니오 |  | 호출 시작시간 |
| `end_time` | `TIMESTAMP` | 예 |  | 호출 종료시간 |
| `driver_seq` | `BIGINT` | 예 | 기록용, FK 없음 | 기사 시퀀스 |
| `response_code` | `VARCHAR(20)` | 예 |  | 응답 코드 |
| `created_at` | `TIMESTAMP` | 아니오 |  | 등록일시 |

**키/인덱스**

- `PK_t_api_call`: `api_call_seq` PK
- `driver_seq`는 로그에 남기는 식별자이며 `t_driver` FK가 아니다.

### 4.12 `t_admin` — 관리자

**목적:** 관리자 로그인과 관리자 계정 상태를 관리한다. 다른 업무 테이블과 직접 FK로 연결되지 않는 독립 테이블이다.

| 컬럼 | 타입 | NULL | 키/비고 | 설명 |
|---|---|---:|---|---|
| `admin_seq` | `BIGSERIAL` | 아니오 | PK | 관리자 시퀀스 |
| `email` | `VARCHAR(50)` | 아니오 | UK | 관리자 이메일 |
| `admin_name` | `VARCHAR(30)` | 아니오 |  | 관리자명 |
| `password` | `VARCHAR(100)` | 아니오 |  | 암호화된 비밀번호 |
| `contact` | `VARCHAR(20)` | 아니오 |  | 연락처 |
| `position` | `VARCHAR(30)` | 예 |  | 직급/직책 |
| `fail_count` | `INTEGER` | 아니오 |  | 로그인 실패 횟수 |
| `password_changed_at` | `TIMESTAMP` | 예 |  | 비밀번호 변경일시 |
| `last_accessed_at` | `TIMESTAMP` | 예 |  | 마지막 성공 로그인 일시 |
| `refresh_token` | `VARCHAR(255)` | 예 |  | 리프레시 토큰 |
| `status_cd` | `VARCHAR(50)` | 아니오 | 코드 | 관리자 상태 코드 |
| `created_at` | `TIMESTAMP` | 아니오 |  | 등록일시 |
| `creator_seq` | `BIGINT` | 아니오 |  | 등록자 |
| `modified_at` | `TIMESTAMP` | 아니오 |  | 수정일시 |
| `modifier_seq` | `BIGINT` | 아니오 |  | 수정자 |

**키/인덱스**

- 기존 제약 이름은 `t_portal_user_pkey`이며 실제 PK 컬럼은 `admin_seq`다.
- `UK_t_admin`: `email` 유니크

### 4.13 `t_common_code_group` — 공통코드 그룹

**목적:** 상태/유형 코드의 분류를 정의한다. 코드 자체는 `t_common_code`에 저장한다.

| 컬럼 | 타입 | NULL | 키/비고 | 설명 |
|---|---|---:|---|---|
| `com_code_group_cd` | `VARCHAR(50)` | 아니오 | PK | 공통코드 그룹 코드 |
| `com_code_group_name` | `VARCHAR(100)` | 아니오 |  | 공통코드 그룹명 |
| `created_at` | `TIMESTAMP` | 아니오 |  | 등록일시 |
| `creator_seq` | `BIGINT` | 아니오 |  | 등록자 |
| `modified_at` | `TIMESTAMP` | 아니오 |  | 수정일시 |
| `modifier_seq` | `BIGINT` | 아니오 |  | 수정자 |

**키/인덱스**

- `t_common_code_group_pkey`: `com_code_group_cd` PK

### 4.14 `t_common_code` — 공통코드 상세

**목적:** 각 코드 그룹에 속한 실제 코드 값과 표시명, 정렬 순서, 사용 여부를 관리한다.

| 컬럼 | 타입 | NULL | 키/비고 | 설명 |
|---|---|---:|---|---|
| `com_code_seq` | `BIGSERIAL` | 아니오 | PK | 공통코드 시퀀스 |
| `com_code_group_cd` | `VARCHAR(50)` | 아니오 | FK → `t_common_code_group.com_code_group_cd` | 공통코드 그룹 |
| `com_code_cd` | `VARCHAR(50)` | 아니오 | UK(복합) | 공통코드 값 |
| `com_code_name` | `VARCHAR(100)` | 아니오 |  | 공통코드명 |
| `sort_order` | `SMALLINT` | 아니오 |  | 정렬 순서 |
| `usage_yn` | `CHAR(1)` | 아니오 |  | 사용 여부 |
| `attribute1` | `VARCHAR(100)` | 예 |  | 속성 1 |
| `attribute2` | `VARCHAR(100)` | 예 |  | 속성 2 |
| `attribute3` | `VARCHAR(100)` | 예 |  | 속성 3 |
| `created_at` | `TIMESTAMP` | 아니오 |  | 등록일시 |
| `creator_seq` | `BIGINT` | 아니오 |  | 등록자 |
| `modified_at` | `TIMESTAMP` | 아니오 |  | 수정일시 |
| `modifier_seq` | `BIGINT` | 아니오 |  | 수정자 |

**키/인덱스**

- `t_common_code_pkey`: `com_code_seq` PK
- `UK_t_common_code`: `(com_code_group_cd, com_code_cd)` 복합 유니크

## 5. 공통코드 초기값

`initData.sql`에 다음 코드 그룹이 정의되어 있다. 업무 테이블의 `*_cd` 컬럼은 DB FK가 아니라 이 테이블의 코드값과 애플리케이션 검증기를 통해 연결된다.

| 그룹 코드 | 그룹명 | 주요 사용처 |
|---|---|---|
| `AdminStatus` | 관리자상태 | `t_admin.status_cd` |
| `DriverStatus` | 기사상태 | `t_driver.status_cd` |
| `VehicleType` | 차량유형 | `t_vehicle.vehicle_type_cd` |
| `FuelType` | 연료구분 | `t_vehicle.fuel_type_cd` |
| `VehicleUseType` | 자용구분 | `t_vehicle.vehicle_use_type_cd` |
| `CenterType` | 센터유형 | `t_center.center_type_cd` |
| `DispatchStatus` | 배차상태 | `t_dispatch.dispatch_status_cd` |
| `DeliveryType` | 배송유형 | `t_delivery.delivery_type_cd` |
| `DeliveryStatus` | 배송상태 | `t_delivery.delivery_status_cd`, `t_delivery_history.delivery_status_cd` |
| `ShootingImpossibleReason` | 촬영불가사유 | `t_delivery.shooting_impossible_reason_cd` |
| `ConsignmentLocation` | 위탁장소 | `t_delivery.consignment_location_cd` |
| `UncompletedReason` | 미배송(미수거)사유 | `t_delivery.uncompleted_reason_cd` |

초기 코드값은 다음과 같다.

- 관리자: `Normal`, `Dormancy`, `Locked`, `Disabled`
- 기사: `WORKING`, `ABSENCE`, `RETIRED`
- 차량 유형: `CARGO`, `BOX_TRUCK`, `WING_BODY`
- 연료: `LPG`, `GASOLINE`, `DIESEL`, `ELECTRIC`
- 자용 구분: `RENTED`, `OWN`
- 센터 유형: `DELIVERY`, `SUPERMARKET`
- 배차 상태: `DISPATCH_WAITING`, `DISPATCHED`, `INSPECTION_COMPLETED`, `IN_DELIVERY`, `COMPLETED`
- 배송 유형: `DELIVERY`, `COLLECTION`
- 배송 상태: `INSPECTION_WAITING`, `INSPECTION_COMPLETED`, `IN_DELIVERY`, `COMPLETED`, `UNCOMPLETED`
- 촬영 불가 사유: `BROKEN_CAMERA`, `SERVER_ERROR`, `ETC`
- 위탁 장소: `FRONT_OF_DOOR`, `SECURITY_OFFICE`, `DELIVERY_BOX`, `ETC`
- 미완료 사유: `EXCESSIVE_QUANTITY`, `CUSTOMER_REQUEST`, `NO_ENTRY_BUILDING`, `LOST_PARCEL`, `ETC`

새 코드값이 필요한 경우 컬럼이나 Java enum만 임의로 늘리지 말고, 애플리케이션 검증 규칙과 `t_common_code` 초기/운영 데이터를 함께 갱신한다.

### 5.1 상태 흐름 해석

초기 코드의 `sort_order`와 이름에서 다음 흐름을 추정할 수 있지만, SQL에는 상태 전이 규칙이 정의되어 있지 않다.

- 배차: `DISPATCH_WAITING` → `DISPATCHED` → `INSPECTION_COMPLETED` → `IN_DELIVERY` → `COMPLETED`
- 배송: `INSPECTION_WAITING` → `INSPECTION_COMPLETED` → `IN_DELIVERY` → `COMPLETED` 또는 `UNCOMPLETED`

DB는 임의의 상태 변경, 역전 전이, 중복 상태, 완료일시와 상태의 불일치를 막지 않는다. 실제 전이 검증은 애플리케이션 책임이다.

## 6. 개발용 샘플 데이터

`testData.sql`은 현재 다음 데이터를 제공한다.

- 센터 2건
- 기사 2건
- 권역 20건
- 권역 범위 20건
- 차량 20건

배차·배송·배송이력·첨부·파일·API 호출 데이터는 포함하지 않는다. 따라서 이 SQL만으로 `배차 → 배송 → 완료/미완료`의 전체 운영 시나리오는 확인할 수 없다.

샘플 데이터는 센터·기사·권역을 이름 또는 `center_seq`/`driver_seq` 조회로 참조해 차량을 생성한다. 첫 두 차량의 배정 기사는 차량과 서로 다른 센터 소속이어서, FK는 유효하지만 센터 기준으로는 교차 배정이다. 의도된 테스트 데이터인지 확인이 필요하다.

`testData.sql`의 권역 범위 중 하나는 파일에 `하산운`으로 기록되어 있어 실제 주소 데이터와 대조해야 한다. 또한 `initData.sql`과 `testData.sql`에는 `ON CONFLICT`나 초기화 구문이 없으므로 빈 DB가 아닌 환경에서 그대로 재실행하면 PK/UK 충돌이 발생할 수 있다. 샘플 SQL에 계정 비밀번호나 운영 비밀값은 문서화하지 않는다.

## 7. 인덱스 및 운영 참고

명시적으로 정의된 일반 인덱스는 `IX_t_delivery(dispatch_seq, vehicle_seq)` 하나다. 다음 자식 FK 컬럼에는 별도 인덱스가 없다.

- `t_driver.center_seq`
- `t_vehicle.center_seq`, `t_vehicle.region_seq`
- `t_dispatch.center_seq`
- `t_delivery.driver_seq`, `t_delivery.region_seq`, `t_delivery.delivery_photo_attach_seq`
- `t_delivery_history.delivery_seq`
- `t_file.attach_seq`
- `t_api_call.driver_seq`

PostgreSQL은 FK를 선언한다고 자식 쪽 인덱스를 자동 생성하지 않는다. 부모 삭제 검증이나 해당 컬럼 조회가 많은 경우 데이터量和 실행 계획에 따라 인덱스 추가를 검토한다.

## 8. 관련 파일

- 스키마 정의: `backend/database/createTable.sql`
- 초기 공통코드: `backend/database/initData.sql`
- 개발용 샘플 데이터: `backend/database/testData.sql`
- API 문서: `backend/docs/api.md`
- MyBatis 설정: `backend/tms-library/tms-core/src/main/resources/mybatis/mybatis-config.xml`
- Mapper XML: `backend/tms-library/tms-core/src/main/resources/mappers/`
