-- 차량
CREATE TABLE "t_vehicle"
(
    "vehicle_seq"          BIGSERIAL    NOT NULL, -- 차량시퀀스
    "center_seq"           BIGINT       NOT NULL, -- 센터시퀀스
    "driver_seq"           BIGINT       NULL,     -- 기사시퀀스
    "region_seq"           BIGINT       NULL,     -- 권역시퀀스
    "vehicle_name"         VARCHAR(100) NOT NULL, -- 차량명
    "model"                VARCHAR(50)  NOT NULL, -- 차종
    "vehicle_type_cd"      VARCHAR(50)  NOT NULL, -- 차량유형코드
    "vehicle_num"          VARCHAR(10)  NOT NULL, -- 차량번호
    "vehicle_reg_num"      VARCHAR(10)  NOT NULL, -- 차량등록번호
    "model_year"           VARCHAR(4)   NOT NULL, -- 연식
    "vehicle_reg_area"     VARCHAR(50)  NULL,     -- 차량등록지
    "owner_name"           VARCHAR(30)  NOT NULL, -- 차량소유자명
    "length"               VARCHAR(20)  NOT NULL, -- 길이
    "width"                VARCHAR(20)  NOT NULL, -- 넓이
    "ton_grade"            VARCHAR(20)  NOT NULL, -- 톤급
    "max_loading_capacity" VARCHAR(20)  NOT NULL, -- 최대적재량
    "fuel_type_cd"         VARCHAR(50)  NOT NULL, -- 연료구분코드
    "fuel_efficiency"      VARCHAR(20)  NOT NULL, -- 연비
    "vehicle_use_type_cd"  VARCHAR(50)  NOT NULL, -- 자용구분코드
    "garage_name"          VARCHAR(100) NOT NULL, -- 차고지명
    "usage_yn"             CHAR(1)      NOT NULL, -- 사용여부
    "created_at"           TIMESTAMP    NOT NULL, -- 등록일시
    "creator_seq"          BIGINT       NOT NULL, -- 등록자
    "modified_at"          TIMESTAMP    NOT NULL, -- 수정일시
    "modifier_seq"         BIGINT       NOT NULL  -- 수정자
);

-- 차량
COMMENT ON TABLE "t_vehicle" IS '차량';

-- 차량시퀀스
COMMENT ON COLUMN "t_vehicle"."vehicle_seq" IS '차량시퀀스';

-- 센터시퀀스
COMMENT ON COLUMN "t_vehicle"."center_seq" IS '센터시퀀스';

-- 기사시퀀스
COMMENT ON COLUMN "t_vehicle"."driver_seq" IS '기사시퀀스';

-- 권역시퀀스
COMMENT ON COLUMN "t_vehicle"."region_seq" IS '권역시퀀스';

-- 차량명
COMMENT ON COLUMN "t_vehicle"."vehicle_name" IS '차량명';

-- 차종
COMMENT ON COLUMN "t_vehicle"."model" IS '차종';

-- 차량유형코드
COMMENT ON COLUMN "t_vehicle"."vehicle_type_cd" IS '차량유형코드';

-- 차량번호
COMMENT ON COLUMN "t_vehicle"."vehicle_num" IS '차량번호';

-- 차량등록번호
COMMENT ON COLUMN "t_vehicle"."vehicle_reg_num" IS '차량등록번호';

-- 연식
COMMENT ON COLUMN "t_vehicle"."model_year" IS '연식';

-- 차량등록지
COMMENT ON COLUMN "t_vehicle"."vehicle_reg_area" IS '차량등록지';

-- 차량소유자명
COMMENT ON COLUMN "t_vehicle"."owner_name" IS '차량소유자명';

-- 길이
COMMENT ON COLUMN "t_vehicle"."length" IS '길이';

-- 넓이
COMMENT ON COLUMN "t_vehicle"."width" IS '넓이';

-- 톤급
COMMENT ON COLUMN "t_vehicle"."ton_grade" IS '톤급';

-- 최대적재량
COMMENT ON COLUMN "t_vehicle"."max_loading_capacity" IS '최대적재량';

-- 연료구분코드
COMMENT ON COLUMN "t_vehicle"."fuel_type_cd" IS '연료구분코드';

-- 연비
COMMENT ON COLUMN "t_vehicle"."fuel_efficiency" IS '연비';

-- 자용구분코드
COMMENT ON COLUMN "t_vehicle"."vehicle_use_type_cd" IS '자용구분코드';

-- 차고지명
COMMENT ON COLUMN "t_vehicle"."garage_name" IS '차고지명';

-- 사용여부
COMMENT ON COLUMN "t_vehicle"."usage_yn" IS '사용여부';

-- 등록일시
COMMENT ON COLUMN "t_vehicle"."created_at" IS '등록일시';

-- 등록자
COMMENT ON COLUMN "t_vehicle"."creator_seq" IS '등록자';

-- 수정일시
COMMENT ON COLUMN "t_vehicle"."modified_at" IS '수정일시';

-- 수정자
COMMENT ON COLUMN "t_vehicle"."modifier_seq" IS '수정자';

-- t_vehicle_info_pk
CREATE UNIQUE INDEX "t_vehicle_info_pk"
    ON "t_vehicle"
        ( -- 차량
         "vehicle_seq" ASC NULLS LAST -- 차량시퀀스
            );

-- t_vehicle_info_pk
COMMENT ON INDEX "t_vehicle_info_pk" IS 't_vehicle_info_pk';

-- 차량 유니크 인덱스
CREATE UNIQUE INDEX "UIX_t_vehicle"
    ON "t_vehicle"
        ( -- 차량
         "driver_seq" ASC NULLS LAST -- 기사시퀀스
            );

-- 차량 유니크 인덱스
COMMENT ON INDEX "UIX_t_vehicle" IS '차량 유니크 인덱스';

-- 차량
ALTER TABLE "t_vehicle"
    ADD CONSTRAINT "t_vehicle_info_pk"
        -- t_vehicle_info_pk
        PRIMARY KEY
            USING INDEX "t_vehicle_info_pk"
            NOT DEFERRABLE;

-- t_vehicle_info_pk
COMMENT ON CONSTRAINT "t_vehicle_info_pk" ON "t_vehicle" IS 't_vehicle_info_pk';

-- 차량
ALTER TABLE "t_vehicle"
    ADD CONSTRAINT "UK_t_vehicle" -- 차량 유니크 제약
        UNIQUE
            USING INDEX "UIX_t_vehicle"
            NOT DEFERRABLE;

-- 차량 유니크 제약
COMMENT ON CONSTRAINT "UK_t_vehicle" ON "t_vehicle" IS '차량 유니크 제약';

-- 센터
CREATE TABLE "t_center"
(
    "center_seq"     BIGSERIAL      NOT NULL, -- 센터시퀀스
    "center_name"    VARCHAR(100)   NOT NULL, -- 센터명
    "center_type_cd" VARCHAR(50)    NOT NULL, -- 센터유형코드
    "center_addr_1"  VARCHAR(100)   NOT NULL, -- 주소
    "center_addr_2"  VARCHAR(100)   NOT NULL, -- 상세주소
    "zip_code"       VARCHAR(10)    NOT NULL, -- 우편번호
    "latitude"       NUMERIC(10, 7) NOT NULL, -- 위도
    "longitude"      NUMERIC(10, 7) NOT NULL, -- 경도
    "total_area"     NUMERIC        NULL,     -- 총면적
    "manager_name"   VARCHAR(30)    NOT NULL, -- 담당자
    "contact_num"    VARCHAR(20)    NOT NULL, -- 연락처
    "usage_yn"       CHAR(1)        NOT NULL, -- 사용여부
    "created_at"     TIMESTAMP      NOT NULL, -- 등록일시
    "creator_seq"    BIGINT         NOT NULL, -- 등록자
    "modified_at"    TIMESTAMP      NOT NULL, -- 수정일시
    "modifier_seq"   BIGINT         NOT NULL  -- 수정자
);

-- 센터
COMMENT ON TABLE "t_center" IS '센터';

-- 센터시퀀스
COMMENT ON COLUMN "t_center"."center_seq" IS '센터시퀀스';

-- 센터명
COMMENT ON COLUMN "t_center"."center_name" IS '센터명';

-- 센터유형코드
COMMENT ON COLUMN "t_center"."center_type_cd" IS '센터유형코드';

-- 주소
COMMENT ON COLUMN "t_center"."center_addr_1" IS '주소';

-- 상세주소
COMMENT ON COLUMN "t_center"."center_addr_2" IS '상세주소';

-- 우편번호
COMMENT ON COLUMN "t_center"."zip_code" IS '우편번호';

-- 위도
COMMENT ON COLUMN "t_center"."latitude" IS '위도';

-- 경도
COMMENT ON COLUMN "t_center"."longitude" IS '경도';

-- 총면적
COMMENT ON COLUMN "t_center"."total_area" IS '총면적';

-- 담당자
COMMENT ON COLUMN "t_center"."manager_name" IS '담당자';

-- 연락처
COMMENT ON COLUMN "t_center"."contact_num" IS '연락처';

-- 사용여부
COMMENT ON COLUMN "t_center"."usage_yn" IS '사용여부';

-- 등록일시
COMMENT ON COLUMN "t_center"."created_at" IS '등록일시';

-- 등록자
COMMENT ON COLUMN "t_center"."creator_seq" IS '등록자';

-- 수정일시
COMMENT ON COLUMN "t_center"."modified_at" IS '수정일시';

-- 수정자
COMMENT ON COLUMN "t_center"."modifier_seq" IS '수정자';

-- t_center_info_pk
CREATE UNIQUE INDEX "t_center_info_pk"
    ON "t_center"
        ( -- 센터
         "center_seq" ASC NULLS LAST -- 센터시퀀스
            );

-- t_center_info_pk
COMMENT ON INDEX "t_center_info_pk" IS 't_center_info_pk';

-- 센터
ALTER TABLE "t_center"
    ADD CONSTRAINT "t_center_info_pk"
        -- t_center_info_pk
        PRIMARY KEY
            USING INDEX "t_center_info_pk"
            NOT DEFERRABLE;

-- t_center_info_pk
COMMENT ON CONSTRAINT "t_center_info_pk" ON "t_center" IS 't_center_info_pk';

-- 권역
CREATE TABLE "t_region"
(
    "region_seq"   BIGSERIAL    NOT NULL, -- 권역시퀀스
    "region_name"  VARCHAR(100) NOT NULL, -- 권역명
    "created_at"   TIMESTAMP    NOT NULL, -- 등록일시
    "creator_seq"  BIGINT       NOT NULL, -- 등록자
    "modified_at"  TIMESTAMP    NOT NULL, -- 수정일시
    "modifier_seq" BIGINT       NOT NULL  -- 수정자
);

-- 권역
COMMENT ON TABLE "t_region" IS '권역';

-- 권역시퀀스
COMMENT ON COLUMN "t_region"."region_seq" IS '권역시퀀스';

-- 권역명
COMMENT ON COLUMN "t_region"."region_name" IS '권역명';

-- 등록일시
COMMENT ON COLUMN "t_region"."created_at" IS '등록일시';

-- 등록자
COMMENT ON COLUMN "t_region"."creator_seq" IS '등록자';

-- 수정일시
COMMENT ON COLUMN "t_region"."modified_at" IS '수정일시';

-- 수정자
COMMENT ON COLUMN "t_region"."modifier_seq" IS '수정자';

-- t_region_info_pk
CREATE UNIQUE INDEX "t_region_info_pk"
    ON "t_region"
        ( -- 권역
         "region_seq" ASC NULLS LAST -- 권역시퀀스
            );

-- t_region_info_pk
COMMENT ON INDEX "t_region_info_pk" IS 't_region_info_pk';

-- 권역
ALTER TABLE "t_region"
    ADD CONSTRAINT "t_region_info_pk"
        -- t_region_info_pk
        PRIMARY KEY
            USING INDEX "t_region_info_pk"
            NOT DEFERRABLE;

-- t_region_info_pk
COMMENT ON CONSTRAINT "t_region_info_pk" ON "t_region" IS 't_region_info_pk';

-- 기사
CREATE TABLE "t_driver"
(
    "driver_seq"            BIGSERIAL    NOT NULL, -- 기사시퀀스
    "driver_name"           VARCHAR(30)  NOT NULL, -- 기사명
    "center_seq"            BIGINT       NOT NULL, -- 센터시퀀스
    "login_id"              VARCHAR(50)  NOT NULL, -- 로그인아이디
    "password"              VARCHAR(100) NOT NULL, -- 비밀번호
    "ext_system_linked_id"  VARCHAR(50)  NOT NULL, -- 외부시스템연동아이디
    "driver_phone_num"      VARCHAR(20)  NOT NULL, -- 연락처
    "email"                 VARCHAR(50)  NOT NULL, -- 이메일
    "driver_license_num"    VARCHAR(15)  NOT NULL, -- 운전면허번호
    "driver_license_date"   DATE         NOT NULL, -- 운전면허발급일
    "driver_license_agency" varchar(20)  NOT NULL, -- 운전면허발급기관
    "working_days"          VARCHAR(20)  NULL,     -- 근무요일
    "working_start_hour"    TIME         NULL,     -- 근무시작시간
    "working_end_hour"      TIME         NULL,     -- 근무종료시간
    "status_cd"             VARCHAR(50)  NOT NULL, -- 상태
    "comp_name"             VARCHAR(100) NULL,     -- 소속업체명
    "created_at"            TIMESTAMP    NOT NULL, -- 등록일시
    "creator_seq"           BIGINT       NOT NULL, -- 등록자
    "modified_at"           TIMESTAMP    NOT NULL, -- 수정일시
    "modifier_seq"          BIGINT       NOT NULL  -- 수정자
);

-- 기사
COMMENT ON TABLE "t_driver" IS '기사';

-- 기사시퀀스
COMMENT ON COLUMN "t_driver"."driver_seq" IS '기사시퀀스';

-- 기사명
COMMENT ON COLUMN "t_driver"."driver_name" IS '기사명';

-- 센터시퀀스
COMMENT ON COLUMN "t_driver"."center_seq" IS '센터시퀀스';

-- 로그인아이디
COMMENT ON COLUMN "t_driver"."login_id" IS '로그인아이디';

-- 비밀번호
COMMENT ON COLUMN "t_driver"."password" IS '비밀번호';

-- 외부시스템연동아이디
COMMENT ON COLUMN "t_driver"."ext_system_linked_id" IS '외부시스템연동아이디';

-- 연락처
COMMENT ON COLUMN "t_driver"."driver_phone_num" IS '연락처';

-- 이메일
COMMENT ON COLUMN "t_driver"."email" IS '이메일';

-- 운전면허번호
COMMENT ON COLUMN "t_driver"."driver_license_num" IS '운전면허번호';

-- 운전면허발급일
COMMENT ON COLUMN "t_driver"."driver_license_date" IS '운전면허발급일';

-- 운전면허발급기관
COMMENT ON COLUMN "t_driver"."driver_license_agency" IS '운전면허발급기관';

-- 근무요일
COMMENT ON COLUMN "t_driver"."working_days" IS '근무요일';

-- 근무시작시간
COMMENT ON COLUMN "t_driver"."working_start_hour" IS '근무시작시간';

-- 근무종료시간
COMMENT ON COLUMN "t_driver"."working_end_hour" IS '근무종료시간';

-- 상태
COMMENT ON COLUMN "t_driver"."status_cd" IS '상태';

-- 소속업체명
COMMENT ON COLUMN "t_driver"."comp_name" IS '소속업체명';

-- 등록일시
COMMENT ON COLUMN "t_driver"."created_at" IS '등록일시';

-- 등록자
COMMENT ON COLUMN "t_driver"."creator_seq" IS '등록자';

-- 수정일시
COMMENT ON COLUMN "t_driver"."modified_at" IS '수정일시';

-- 수정자
COMMENT ON COLUMN "t_driver"."modifier_seq" IS '수정자';

-- t_driver_info_pk
CREATE UNIQUE INDEX "t_driver_info_pk"
    ON "t_driver"
        ( -- 기사
         "driver_seq" ASC NULLS LAST -- 기사시퀀스
            );

-- t_driver_info_pk
COMMENT ON INDEX "t_driver_info_pk" IS 't_driver_info_pk';

-- 기사 유니크 인덱스
CREATE UNIQUE INDEX "UIX_t_driver"
    ON "t_driver"
        ( -- 기사
         "login_id" ASC NULLS LAST -- 로그인아이디
            );

-- 기사 유니크 인덱스
COMMENT ON INDEX "UIX_t_driver" IS '기사 유니크 인덱스';

-- 기사 유니크 인덱스2
CREATE UNIQUE INDEX "UIX_t_driver2"
    ON "t_driver"
        ( -- 기사
         "ext_system_linked_id" ASC NULLS LAST -- 외부시스템연동아이디
            );

-- 기사 유니크 인덱스2
COMMENT ON INDEX "UIX_t_driver2" IS '기사 유니크 인덱스2';

-- 기사
ALTER TABLE "t_driver"
    ADD CONSTRAINT "t_driver_info_pk"
        -- t_driver_info_pk
        PRIMARY KEY
            USING INDEX "t_driver_info_pk"
            NOT DEFERRABLE;

-- t_driver_info_pk
COMMENT ON CONSTRAINT "t_driver_info_pk" ON "t_driver" IS 't_driver_info_pk';

-- 기사
ALTER TABLE "t_driver"
    ADD CONSTRAINT "UK_t_driver" -- 기사 유니크 제약
        UNIQUE
            USING INDEX "UIX_t_driver"
            NOT DEFERRABLE;

-- 기사 유니크 제약
COMMENT ON CONSTRAINT "UK_t_driver" ON "t_driver" IS '기사 유니크 제약';

-- 기사
ALTER TABLE "t_driver"
    ADD CONSTRAINT "UK_t_driver2" -- 기사 유니크 제약2
        UNIQUE
            USING INDEX "UIX_t_driver2"
            NOT DEFERRABLE;

-- 기사 유니크 제약2
COMMENT ON CONSTRAINT "UK_t_driver2" ON "t_driver" IS '기사 유니크 제약2';

-- 배송
CREATE TABLE "t_delivery"
(
    "delivery_seq"                  BIGSERIAL      NOT NULL, -- 배송시퀀스
    "dispatch_seq"                  BIGINT         NOT NULL, -- 배차시퀀스
    "vehicle_seq"                   BIGINT         NULL,     -- 차량시퀀스
    "driver_seq"                    BIGINT         NULL,     -- 기사시퀀스
    "region_seq"                    BIGINT         NULL,     -- 권역시퀀스
    "delivery_order"                SMALLINT       NOT NULL, -- 배송순서
    "tracking_num"                  VARCHAR(50)    NOT NULL, -- 운송장번호(배송번호)
    "recipient_name"                VARCHAR(30)    NOT NULL, -- 수령인이름
    "recipient_phone_num"           VARCHAR(20)    NOT NULL, -- 수령인핸드폰번호
    "delivery_postal_code"          VARCHAR(10)    NOT NULL, -- 배송지우편번호
    "delivery_addr_1"               VARCHAR(100)   NOT NULL, -- 배송지주소
    "delivery_addr_2"               VARCHAR(100)   NOT NULL, -- 배송지상세주소
    "delivery_type_cd"              VARCHAR(50)    NOT NULL, -- 배송유형(배송/수거)
    "product_name"                  VARCHAR(100)   NOT NULL, -- 상품명
    "box_count"                     SMALLINT       NOT NULL, -- 박스수량
    "memo"                          VARCHAR(1000)  NULL,     -- 배송메모
    "security_code"                 VARCHAR(50)    NULL,     -- 출입번호
    "pickup_datetime"               TIMESTAMP      NULL,     -- 집하일시(택배)
    "latitude"                      NUMERIC(10, 7) NOT NULL, -- 위도
    "longitude"                     NUMERIC(10, 7) NOT NULL, -- 경도
    "delivery_status_cd"            VARCHAR(50)    NOT NULL, -- 배송상태코드
    "delivery_estimated_start_time" TIME           NULL,     -- 배송예정시작시간
    "delivery_estimated_end_time"   TIME           NULL,     -- 배송예정종료시간
    "delivery_datetime"             TIMESTAMP      NULL,     -- 배송완료일시
    "shooting_impossible_yn"        CHAR(1)        NULL,     -- 촬영불가여부
    "shooting_impossible_reason_cd" VARCHAR(50)    NULL,     -- 촬영불가사유코드
    "consignment_location_cd"       VARCHAR(50)    NULL,     -- 위탁장소코드
    "uncompleted_reason_cd"         VARCHAR(50)    NULL,     -- 미완료사유코드
    "delivery_photo_attach_seq"     BIGINT         NULL,     -- 배송사진첨부시퀀스
    "created_at"                    TIMESTAMP      NOT NULL, -- 등록일시
    "creator_seq"                   BIGINT         NOT NULL, -- 등록자
    "modified_at"                   TIMESTAMP      NOT NULL, -- 수정일시
    "modifier_seq"                  BIGINT         NOT NULL  -- 수정자
);

-- 배송
COMMENT ON TABLE "t_delivery" IS '배송';

-- 배송시퀀스
COMMENT ON COLUMN "t_delivery"."delivery_seq" IS '배송시퀀스';

-- 배차시퀀스
COMMENT ON COLUMN "t_delivery"."dispatch_seq" IS '배차시퀀스';

-- 차량시퀀스
COMMENT ON COLUMN "t_delivery"."vehicle_seq" IS '차량시퀀스';

-- 기사시퀀스
COMMENT ON COLUMN "t_delivery"."driver_seq" IS '기사시퀀스';

-- 권역시퀀스
COMMENT ON COLUMN "t_delivery"."region_seq" IS '권역시퀀스';

-- 배송순서
COMMENT ON COLUMN "t_delivery"."delivery_order" IS '배송순서';

-- 운송장번호(배송번호)
COMMENT ON COLUMN "t_delivery"."tracking_num" IS '운송장번호(배송번호)';

-- 수령인이름
COMMENT ON COLUMN "t_delivery"."recipient_name" IS '수령인이름';

-- 수령인핸드폰번호
COMMENT ON COLUMN "t_delivery"."recipient_phone_num" IS '수령인핸드폰번호';

-- 배송지우편번호
COMMENT ON COLUMN "t_delivery"."delivery_postal_code" IS '배송지우편번호';

-- 배송지주소
COMMENT ON COLUMN "t_delivery"."delivery_addr_1" IS '배송지주소';

-- 배송지상세주소
COMMENT ON COLUMN "t_delivery"."delivery_addr_2" IS '배송지상세주소';

-- 배송유형(배송/수거)
COMMENT ON COLUMN "t_delivery"."delivery_type_cd" IS '배송유형(배송/수거)';

-- 상품명
COMMENT ON COLUMN "t_delivery"."product_name" IS '상품명';

-- 박스수량
COMMENT ON COLUMN "t_delivery"."box_count" IS '박스수량';

-- 배송메모
COMMENT ON COLUMN "t_delivery"."memo" IS '배송메모';

-- 출입번호
COMMENT ON COLUMN "t_delivery"."security_code" IS '출입번호';

-- 집하일시(택배)
COMMENT ON COLUMN "t_delivery"."pickup_datetime" IS '집하일시(택배)';

-- 위도
COMMENT ON COLUMN "t_delivery"."latitude" IS '위도';

-- 경도
COMMENT ON COLUMN "t_delivery"."longitude" IS '경도';

-- 배송상태코드
COMMENT ON COLUMN "t_delivery"."delivery_status_cd" IS '배송상태코드';

-- 배송예정시작시간
COMMENT ON COLUMN "t_delivery"."delivery_estimated_start_time" IS '배송예정시작시간';

-- 배송예정종료시간
COMMENT ON COLUMN "t_delivery"."delivery_estimated_end_time" IS '배송예정종료시간';

-- 배송완료일시
COMMENT ON COLUMN "t_delivery"."delivery_datetime" IS '배송완료일시';

-- 촬영불가여부
COMMENT ON COLUMN "t_delivery"."shooting_impossible_yn" IS '촬영불가여부';

-- 촬영불가사유코드
COMMENT ON COLUMN "t_delivery"."shooting_impossible_reason_cd" IS '촬영불가사유코드';

-- 위탁장소코드
COMMENT ON COLUMN "t_delivery"."consignment_location_cd" IS '위탁장소코드';

-- 미완료사유코드
COMMENT ON COLUMN "t_delivery"."uncompleted_reason_cd" IS '미완료사유코드';

-- 배송사진첨부시퀀스
COMMENT ON COLUMN "t_delivery"."delivery_photo_attach_seq" IS '배송사진첨부시퀀스';

-- 등록일시
COMMENT ON COLUMN "t_delivery"."created_at" IS '등록일시';

-- 등록자
COMMENT ON COLUMN "t_delivery"."creator_seq" IS '등록자';

-- 수정일시
COMMENT ON COLUMN "t_delivery"."modified_at" IS '수정일시';

-- 수정자
COMMENT ON COLUMN "t_delivery"."modifier_seq" IS '수정자';

-- t_order_info_pk
CREATE UNIQUE INDEX "t_delivery_pk"
    ON "t_delivery"
        ( -- 배송
         "delivery_seq" ASC NULLS LAST -- 배송시퀀스
            );

-- t_order_info_pk
COMMENT ON INDEX "t_delivery_pk" IS 't_order_info_pk';

-- 배송 유니크 인덱스
CREATE UNIQUE INDEX "UIX_t_delivery"
    ON "t_delivery"
        ( -- 배송
         "tracking_num" ASC NULLS LAST -- 운송장번호(배송번호)
            );

-- 배송 유니크 인덱스
COMMENT ON INDEX "UIX_t_delivery" IS '배송 유니크 인덱스';

-- 배송 인덱스
CREATE INDEX "IX_t_delivery"
    ON "t_delivery"
        ( -- 배송
         "dispatch_seq" ASC NULLS LAST, -- 배차시퀀스
         "vehicle_seq" ASC NULLS LAST -- 차량시퀀스
            );

-- 배송 인덱스
COMMENT ON INDEX "IX_t_delivery" IS '배송 인덱스';

-- 배송
ALTER TABLE "t_delivery"
    ADD CONSTRAINT "t_delivery_pk"
        -- t_delivery_pk
        PRIMARY KEY
            USING INDEX "t_delivery_pk"
            NOT DEFERRABLE;

-- t_delivery_pk
COMMENT ON CONSTRAINT "t_delivery_pk" ON "t_delivery" IS 't_delivery_pk';

-- 배송
ALTER TABLE "t_delivery"
    ADD CONSTRAINT "UK_t_delivery" -- 배송 유니크 제약
        UNIQUE
            USING INDEX "UIX_t_delivery"
            NOT DEFERRABLE;

-- 배송 유니크 제약
COMMENT ON CONSTRAINT "UK_t_delivery" ON "t_delivery" IS '배송 유니크 제약';

-- 배차
CREATE TABLE "t_dispatch"
(
    "dispatch_seq"       BIGSERIAL    NOT NULL, -- 배차시퀀스
    "center_seq"         BIGINT       NOT NULL, -- 센터시퀀스
    "dispatch_name"      VARCHAR(100) NOT NULL, -- 배차명
    "delivery_date"      DATE         NOT NULL, -- 배송일자
    "dispatch_status_cd" VARCHAR(50)  NOT NULL, -- 배차상태코드
    "created_at"         TIMESTAMP    NOT NULL, -- 등록일시
    "creator_seq"        BIGINT       NOT NULL, -- 등록자
    "modified_at"        TIMESTAMP    NOT NULL, -- 수정일시
    "modifier_seq"       BIGINT       NOT NULL  -- 수정자
);

-- 배차
COMMENT ON TABLE "t_dispatch" IS '배차';

-- 배차시퀀스
COMMENT ON COLUMN "t_dispatch"."dispatch_seq" IS '배차시퀀스';

-- 센터시퀀스
COMMENT ON COLUMN "t_dispatch"."center_seq" IS '센터시퀀스';

-- 배차명
COMMENT ON COLUMN "t_dispatch"."dispatch_name" IS '배차명';

-- 배송일자
COMMENT ON COLUMN "t_dispatch"."delivery_date" IS '배송일자';

-- 배차상태코드
COMMENT ON COLUMN "t_dispatch"."dispatch_status_cd" IS '배차상태코드';

-- 등록일시
COMMENT ON COLUMN "t_dispatch"."created_at" IS '등록일시';

-- 등록자
COMMENT ON COLUMN "t_dispatch"."creator_seq" IS '등록자';

-- 수정일시
COMMENT ON COLUMN "t_dispatch"."modified_at" IS '수정일시';

-- 수정자
COMMENT ON COLUMN "t_dispatch"."modifier_seq" IS '수정자';

-- t_dispatch_pk
CREATE UNIQUE INDEX "t_dispatch_pk"
    ON "t_dispatch"
        ( -- 배차
         "dispatch_seq" ASC NULLS LAST -- 배차시퀀스
            );

-- t_dispatch_pk
COMMENT ON INDEX "t_dispatch_pk" IS 't_dispatch_pk';

-- 배차
ALTER TABLE "t_dispatch"
    ADD CONSTRAINT "t_dispatch_pk"
        -- t_dispatch_pk
        PRIMARY KEY
            USING INDEX "t_dispatch_pk"
            NOT DEFERRABLE;

-- t_dispatch_pk
COMMENT ON CONSTRAINT "t_dispatch_pk" ON "t_dispatch" IS 't_dispatch_pk';

-- 공통코드그룹
CREATE TABLE "t_common_code_group"
(
    "com_code_group_cd"   VARCHAR(50)  NOT NULL, -- 공통코드그룹코드
    "com_code_group_name" VARCHAR(100) NOT NULL, -- 공통코드그룹명
    "created_at"          TIMESTAMP    NOT NULL, -- 등록일시
    "creator_seq"         BIGINT       NOT NULL, -- 등록자
    "modified_at"         TIMESTAMP    NOT NULL, -- 수정일시
    "modifier_seq"        BIGINT       NOT NULL  -- 수정자
);

-- 공통코드그룹
COMMENT ON TABLE "t_common_code_group" IS '공통코드그룹';

-- 공통코드그룹코드
COMMENT ON COLUMN "t_common_code_group"."com_code_group_cd" IS '공통코드그룹코드';

-- 공통코드그룹명
COMMENT ON COLUMN "t_common_code_group"."com_code_group_name" IS '공통코드그룹명';

-- 등록일시
COMMENT ON COLUMN "t_common_code_group"."created_at" IS '등록일시';

-- 등록자
COMMENT ON COLUMN "t_common_code_group"."creator_seq" IS '등록자';

-- 수정일시
COMMENT ON COLUMN "t_common_code_group"."modified_at" IS '수정일시';

-- 수정자
COMMENT ON COLUMN "t_common_code_group"."modifier_seq" IS '수정자';

-- t_common_code_group_pkey
CREATE UNIQUE INDEX "t_common_code_group_pkey"
    ON "t_common_code_group"
        ( -- 공통코드그룹
         "com_code_group_cd" ASC NULLS LAST -- 공통코드그룹코드
            );

-- t_common_code_group_pkey
COMMENT ON INDEX "t_common_code_group_pkey" IS 't_common_code_group_pkey';

-- 공통코드그룹
ALTER TABLE "t_common_code_group"
    ADD CONSTRAINT "t_common_code_group_pkey"
        -- t_common_code_group_pkey
        PRIMARY KEY
            USING INDEX "t_common_code_group_pkey"
            NOT DEFERRABLE;

-- t_common_code_group_pkey
COMMENT ON CONSTRAINT "t_common_code_group_pkey" ON "t_common_code_group" IS 't_common_code_group_pkey';

-- 공통코드
CREATE TABLE "t_common_code"
(
    "com_code_seq"      BIGSERIAL    NOT NULL, -- 공통코드시퀀스
    "com_code_group_cd" VARCHAR(50)  NOT NULL, -- 공통코드그룹코드
    "com_code_cd"       VARCHAR(50)  NOT NULL, -- 공통코드
    "com_code_name"     VARCHAR(100) NOT NULL, -- 공통코드명
    "sort_order"        SMALLINT     NOT NULL, -- 정렬순서
    "usage_yn"          CHAR(1)      NOT NULL, -- 사용여부
    "attribute1"        VARCHAR(100) NULL,     -- 속성1
    "attribute2"        VARCHAR(100) NULL,     -- 속성2
    "attribute3"        VARCHAR(100) NULL,     -- 속성3
    "created_at"        TIMESTAMP    NOT NULL, -- 등록일시
    "creator_seq"       BIGINT       NOT NULL, -- 등록자
    "modified_at"       TIMESTAMP    NOT NULL, -- 수정일시
    "modifier_seq"      BIGINT       NOT NULL  -- 수정자
);

-- 공통코드
COMMENT ON TABLE "t_common_code" IS '공통코드';

-- 공통코드시퀀스
COMMENT ON COLUMN "t_common_code"."com_code_seq" IS '공통코드시퀀스';

-- 공통코드그룹코드
COMMENT ON COLUMN "t_common_code"."com_code_group_cd" IS '공통코드그룹코드';

-- 공통코드
COMMENT ON COLUMN "t_common_code"."com_code_cd" IS '공통코드';

-- 공통코드명
COMMENT ON COLUMN "t_common_code"."com_code_name" IS '공통코드명';

-- 정렬순서
COMMENT ON COLUMN "t_common_code"."sort_order" IS '정렬순서';

-- 사용여부
COMMENT ON COLUMN "t_common_code"."usage_yn" IS '사용여부';

-- 속성1
COMMENT ON COLUMN "t_common_code"."attribute1" IS '속성1';

-- 속성2
COMMENT ON COLUMN "t_common_code"."attribute2" IS '속성2';

-- 속성3
COMMENT ON COLUMN "t_common_code"."attribute3" IS '속성3';

-- 등록일시
COMMENT ON COLUMN "t_common_code"."created_at" IS '등록일시';

-- 등록자
COMMENT ON COLUMN "t_common_code"."creator_seq" IS '등록자';

-- 수정일시
COMMENT ON COLUMN "t_common_code"."modified_at" IS '수정일시';

-- 수정자
COMMENT ON COLUMN "t_common_code"."modifier_seq" IS '수정자';

-- t_common_code_pkey
CREATE UNIQUE INDEX "t_common_code_pkey"
    ON "t_common_code"
        ( -- 공통코드
         "com_code_seq" ASC NULLS LAST -- 공통코드시퀀스
            );

-- t_common_code_pkey
COMMENT ON INDEX "t_common_code_pkey" IS 't_common_code_pkey';

-- 공통코드 유니크 인덱스
CREATE UNIQUE INDEX "UK_t_common_code"
    ON "t_common_code"
        ( -- 공통코드
         "com_code_group_cd" ASC NULLS LAST, -- 공통코드그룹코드
         "com_code_cd" ASC NULLS LAST -- 공통코드
            );

-- 공통코드 유니크 인덱스
COMMENT ON INDEX "UK_t_common_code" IS '공통코드 유니크 인덱스';

-- 공통코드
ALTER TABLE "t_common_code"
    ADD CONSTRAINT "t_common_code_pkey"
        -- t_common_code_pkey
        PRIMARY KEY
            USING INDEX "t_common_code_pkey"
            NOT DEFERRABLE;

-- t_common_code_pkey
COMMENT ON CONSTRAINT "t_common_code_pkey" ON "t_common_code" IS 't_common_code_pkey';

-- 공통코드
ALTER TABLE "t_common_code"
    ADD CONSTRAINT "UK_t_common_code" -- 공통코드 유니크 제약
        UNIQUE
            USING INDEX "UK_t_common_code"
            NOT DEFERRABLE;

-- 공통코드 유니크 제약
COMMENT ON CONSTRAINT "UK_t_common_code" ON "t_common_code" IS '공통코드 유니크 제약';

-- 관리자
CREATE TABLE "t_admin"
(
    "admin_seq"           BIGSERIAL    NOT NULL, -- 관리자시퀀스
    "email"               VARCHAR(50)  NOT NULL, -- 관리자D(Email)
    "admin_name"          VARCHAR(30)  NOT NULL, -- 관리자명
    "password"            VARCHAR(100) NOT NULL, -- 비밀번호
    "contact"             VARCHAR(20)  NOT NULL, -- 연락처
    "position"            VARCHAR(30)  NULL,     -- 직급/직책
    "fail_count"          INTEGER      NOT NULL, -- 로그인실패횟수
    "password_changed_at" TIMESTAMP    NULL,     -- 비밀번호변경일
    "last_accessed_at"    TIMESTAMP    NULL,     -- 마지막성공로그인일시
    "refresh_token"       VARCHAR(255) NULL,     -- 리프레시토큰
    "status_cd"           VARCHAR(50)  NOT NULL, -- 상태코드
    "created_at"          TIMESTAMP    NOT NULL, -- 등록일시
    "creator_seq"         BIGINT       NOT NULL, -- 등록자
    "modified_at"         TIMESTAMP    NOT NULL, -- 수정일시
    "modifier_seq"        BIGINT       NOT NULL  -- 수정자
);

-- 관리자
COMMENT ON TABLE "t_admin" IS '관리자';

-- 관리자시퀀스
COMMENT ON COLUMN "t_admin"."admin_seq" IS '관리자시퀀스';

-- 관리자D(Email)
COMMENT ON COLUMN "t_admin"."email" IS '관리자D(Email)';

-- 관리자명
COMMENT ON COLUMN "t_admin"."admin_name" IS '관리자명';

-- 비밀번호
COMMENT ON COLUMN "t_admin"."password" IS '비밀번호';

-- 연락처
COMMENT ON COLUMN "t_admin"."contact" IS '연락처';

-- 직급/직책
COMMENT ON COLUMN "t_admin"."position" IS '직급/직책';

-- 로그인실패횟수
COMMENT ON COLUMN "t_admin"."fail_count" IS '로그인실패횟수';

-- 비밀번호변경일
COMMENT ON COLUMN "t_admin"."password_changed_at" IS '비밀번호변경일';

-- 마지막성공로그인일시
COMMENT ON COLUMN "t_admin"."last_accessed_at" IS '마지막성공로그인일시';

-- 리프레시토큰
COMMENT ON COLUMN "t_admin"."refresh_token" IS '리프레시토큰';

-- 상태코드
COMMENT ON COLUMN "t_admin"."status_cd" IS '상태코드';

-- 등록일시
COMMENT ON COLUMN "t_admin"."created_at" IS '등록일시';

-- 등록자
COMMENT ON COLUMN "t_admin"."creator_seq" IS '등록자';

-- 수정일시
COMMENT ON COLUMN "t_admin"."modified_at" IS '수정일시';

-- 수정자
COMMENT ON COLUMN "t_admin"."modifier_seq" IS '수정자';

-- t_portal_user_pkey
CREATE UNIQUE INDEX "t_portal_user_pkey"
    ON "t_admin"
        ( -- 관리자
         "admin_seq" ASC NULLS LAST -- 관리자시퀀스
            );

-- t_portal_user_pkey
COMMENT ON INDEX "t_portal_user_pkey" IS 't_portal_user_pkey';

-- 관리자 유니크 인덱스
CREATE UNIQUE INDEX "UIX_t_admin"
    ON "t_admin"
        ( -- 관리자
         "email" ASC NULLS LAST -- 관리자D(Email)
            );

-- 관리자 유니크 인덱스
COMMENT ON INDEX "UIX_t_admin" IS '관리자 유니크 인덱스';

-- 관리자
ALTER TABLE "t_admin"
    ADD CONSTRAINT "t_portal_user_pkey"
        -- t_portal_user_pkey
        PRIMARY KEY
            USING INDEX "t_portal_user_pkey"
            NOT DEFERRABLE;

-- t_portal_user_pkey
COMMENT ON CONSTRAINT "t_portal_user_pkey" ON "t_admin" IS 't_portal_user_pkey';

-- 관리자
ALTER TABLE "t_admin"
    ADD CONSTRAINT "UK_t_admin" -- 관리자 유니크 제약
        UNIQUE
            USING INDEX "UIX_t_admin"
            NOT DEFERRABLE;

-- 관리자 유니크 제약
COMMENT ON CONSTRAINT "UK_t_admin" ON "t_admin" IS '관리자 유니크 제약';

-- 권역범위
CREATE TABLE "t_region_range"
(
    "region_seq"     BIGINT       NOT NULL, -- 권역시퀀스
    "eup_myeon_dong" VARCHAR(100) NOT NULL  -- 읍면동
);

-- 권역범위
COMMENT ON TABLE "t_region_range" IS '권역범위';

-- 권역시퀀스
COMMENT ON COLUMN "t_region_range"."region_seq" IS '권역시퀀스';

-- 읍면동
COMMENT ON COLUMN "t_region_range"."eup_myeon_dong" IS '읍면동';

-- 권역범위 기본키
CREATE UNIQUE INDEX "PK_t_region_range"
    ON "t_region_range"
        ( -- 권역범위
         "region_seq" ASC NULLS LAST, -- 권역시퀀스
         "eup_myeon_dong" ASC NULLS LAST -- 읍면동
            );

-- 권역범위 기본키
COMMENT ON INDEX "PK_t_region_range" IS '권역범위 기본키';

-- 권역범위
ALTER TABLE "t_region_range"
    ADD CONSTRAINT "PK_t_region_range"
        -- 권역범위 기본키
        PRIMARY KEY
            USING INDEX "PK_t_region_range"
            NOT DEFERRABLE;

-- 권역범위 기본키
COMMENT ON CONSTRAINT "PK_t_region_range" ON "t_region_range" IS '권역범위 기본키';

-- 배송이력
CREATE TABLE "t_delivery_history"
(
    "delivery_history_seq" BIGSERIAL   NOT NULL, -- 배송이력시퀀스
    "delivery_seq"         BIGINT      NOT NULL, -- 배송시퀀스
    "dispatch_seq"         BIGINT      NOT NULL, -- 배차시퀀스
    "driver_seq"           BIGINT      NOT NULL, -- 기사시퀀스
    "delivery_status_cd"   VARCHAR(50) NOT NULL, -- 배송상태코드
    "created_at"           TIMESTAMP   NOT NULL, -- 등록일시
    "creator_seq"          BIGINT      NOT NULL  -- 등록자
);

-- 배송이력
COMMENT ON TABLE "t_delivery_history" IS '배송이력';

-- 배송이력시퀀스
COMMENT ON COLUMN "t_delivery_history"."delivery_history_seq" IS '배송이력시퀀스';

-- 배송시퀀스
COMMENT ON COLUMN "t_delivery_history"."delivery_seq" IS '배송시퀀스';

-- 배차시퀀스
COMMENT ON COLUMN "t_delivery_history"."dispatch_seq" IS '배차시퀀스';

-- 기사시퀀스
COMMENT ON COLUMN "t_delivery_history"."driver_seq" IS '기사시퀀스';

-- 배송상태코드
COMMENT ON COLUMN "t_delivery_history"."delivery_status_cd" IS '배송상태코드';

-- 등록일시
COMMENT ON COLUMN "t_delivery_history"."created_at" IS '등록일시';

-- 등록자
COMMENT ON COLUMN "t_delivery_history"."creator_seq" IS '등록자';

-- 배송이력 기본키
CREATE UNIQUE INDEX "PK_t_delivery_history"
    ON "t_delivery_history"
        ( -- 배송이력
         "delivery_history_seq" ASC NULLS LAST -- 배송이력시퀀스
            );

-- 배송이력 기본키
COMMENT ON INDEX "PK_t_delivery_history" IS '배송이력 기본키';

-- 배송이력
ALTER TABLE "t_delivery_history"
    ADD CONSTRAINT "PK_t_delivery_history"
        -- 배송이력 기본키
        PRIMARY KEY
            USING INDEX "PK_t_delivery_history"
            NOT DEFERRABLE;

-- 배송이력 기본키
COMMENT ON CONSTRAINT "PK_t_delivery_history" ON "t_delivery_history" IS '배송이력 기본키';

-- 첨부
CREATE TABLE "t_attach"
(
    "attach_seq"  BIGSERIAL NOT NULL, -- 첨부시퀀스
    "creator_seq" BIGINT    NOT NULL, -- 등록자
    "created_at"  TIMESTAMP NOT NULL  -- 등록일시
);

-- 첨부
COMMENT ON TABLE "t_attach" IS '첨부';

-- 첨부시퀀스
COMMENT ON COLUMN "t_attach"."attach_seq" IS '첨부시퀀스';

-- 등록자
COMMENT ON COLUMN "t_attach"."creator_seq" IS '등록자';

-- 등록일시
COMMENT ON COLUMN "t_attach"."created_at" IS '등록일시';

-- 첨부 기본키
CREATE UNIQUE INDEX "PK_t_attach"
    ON "t_attach"
        ( -- 첨부
         "attach_seq" ASC NULLS LAST -- 첨부시퀀스
            );

-- 첨부 기본키
COMMENT ON INDEX "PK_t_attach" IS '첨부 기본키';

-- 첨부
ALTER TABLE "t_attach"
    ADD CONSTRAINT "PK_t_attach"
        -- 첨부 기본키
        PRIMARY KEY
            USING INDEX "PK_t_attach"
            NOT DEFERRABLE;

-- 첨부 기본키
COMMENT ON CONSTRAINT "PK_t_attach" ON "t_attach" IS '첨부 기본키';

-- 파일
CREATE TABLE "t_file"
(
    "file_seq"           BIGSERIAL    NOT NULL, -- 파일시퀀스
    "attach_seq"         BIGINT       NOT NULL, -- 첨부시퀀스
    "file_full_path"     VARCHAR(255) NOT NULL, -- 파일전체경로
    "original_file_name" VARCHAR(100) NOT NULL, -- 원본파일명
    "sort_order"         SMALLINT     NOT NULL  -- 정렬순서
);

-- 파일
COMMENT ON TABLE "t_file" IS '파일';

-- 파일시퀀스
COMMENT ON COLUMN "t_file"."file_seq" IS '파일시퀀스';

-- 첨부시퀀스
COMMENT ON COLUMN "t_file"."attach_seq" IS '첨부시퀀스';

-- 파일전체경로
COMMENT ON COLUMN "t_file"."file_full_path" IS '파일전체경로';

-- 원본파일명
COMMENT ON COLUMN "t_file"."original_file_name" IS '원본파일명';

-- 정렬순서
COMMENT ON COLUMN "t_file"."sort_order" IS '정렬순서';

-- 파일 기본키
CREATE UNIQUE INDEX "PK_t_file"
    ON "t_file"
        ( -- 파일
         "file_seq" ASC NULLS LAST -- 파일시퀀스
            );

-- 파일 기본키
COMMENT ON INDEX "PK_t_file" IS '파일 기본키';

-- 파일
ALTER TABLE "t_file"
    ADD CONSTRAINT "PK_t_file"
        -- 파일 기본키
        PRIMARY KEY
            USING INDEX "PK_t_file"
            NOT DEFERRABLE;

-- 파일 기본키
COMMENT ON CONSTRAINT "PK_t_file" ON "t_file" IS '파일 기본키';

-- API호출
CREATE TABLE "t_api_call"
(
    "api_call_seq"  BIGSERIAL    NOT NULL, -- API호출시퀀스
    "api_url"       VARCHAR(255) NOT NULL, -- API URL
    "http_method"   VARCHAR(20)  NOT NULL, -- HTTP Method
    "start_time"    TIMESTAMP    NOT NULL, -- API시작시간
    "end_time"      TIMESTAMP    NULL,     -- API종료시간
    "driver_seq"    BIGINT       NULL,     -- 기사시퀀스
    "response_code" VARCHAR(20)  NULL,     -- 응답코드
    "created_at"    TIMESTAMP    NOT NULL  -- 등록일시
);

-- API호출
COMMENT ON TABLE "t_api_call" IS 'API호출';

-- API호출시퀀스
COMMENT ON COLUMN "t_api_call"."api_call_seq" IS 'API호출시퀀스';

-- API URL
COMMENT ON COLUMN "t_api_call"."api_url" IS 'API URL';

-- HTTP Method
COMMENT ON COLUMN "t_api_call"."http_method" IS 'HTTP Method';

-- API시작시간
COMMENT ON COLUMN "t_api_call"."start_time" IS 'API시작시간';

-- API종료시간
COMMENT ON COLUMN "t_api_call"."end_time" IS 'API종료시간';

-- 기사시퀀스
COMMENT ON COLUMN "t_api_call"."driver_seq" IS '기사시퀀스';

-- 응답코드
COMMENT ON COLUMN "t_api_call"."response_code" IS '응답코드';

-- 등록일시
COMMENT ON COLUMN "t_api_call"."created_at" IS '등록일시';

-- API호출 기본키
CREATE UNIQUE INDEX "PK_t_api_call"
    ON "t_api_call"
        ( -- API호출
         "api_call_seq" ASC NULLS LAST -- API호출시퀀스
            );

-- API호출 기본키
COMMENT ON INDEX "PK_t_api_call" IS 'API호출 기본키';

-- API호출
ALTER TABLE "t_api_call"
    ADD CONSTRAINT "PK_t_api_call"
        -- API호출 기본키
        PRIMARY KEY
            USING INDEX "PK_t_api_call"
            NOT DEFERRABLE;

-- API호출 기본키
COMMENT ON CONSTRAINT "PK_t_api_call" ON "t_api_call" IS 'API호출 기본키';

-- 차량
ALTER TABLE "t_vehicle"
    ADD CONSTRAINT "FK_t_center_TO_t_vehicle"
        -- 센터 -> 차량
        FOREIGN KEY (
                     "center_seq" -- 센터시퀀스
            )
            REFERENCES "t_center" ( -- 센터
                                   "center_seq" -- 센터시퀀스
                );

-- 센터 -> 차량
COMMENT ON CONSTRAINT "FK_t_center_TO_t_vehicle" ON "t_vehicle" IS '센터 -> 차량';

-- 차량
ALTER TABLE "t_vehicle"
    ADD CONSTRAINT "FK_t_region_TO_t_vehicle"
        -- 권역 -> 차량
        FOREIGN KEY (
                     "region_seq" -- 권역시퀀스
            )
            REFERENCES "t_region" ( -- 권역
                                   "region_seq" -- 권역시퀀스
                );

-- 권역 -> 차량
COMMENT ON CONSTRAINT "FK_t_region_TO_t_vehicle" ON "t_vehicle" IS '권역 -> 차량';

-- 차량
ALTER TABLE "t_vehicle"
    ADD CONSTRAINT "FK_t_driver_TO_t_vehicle"
        -- 기사 -> 차량
        FOREIGN KEY (
                     "driver_seq" -- 기사시퀀스
            )
            REFERENCES "t_driver" ( -- 기사
                                   "driver_seq" -- 기사시퀀스
                );

-- 기사 -> 차량
COMMENT ON CONSTRAINT "FK_t_driver_TO_t_vehicle" ON "t_vehicle" IS '기사 -> 차량';

-- 기사
ALTER TABLE "t_driver"
    ADD CONSTRAINT "FK_t_center_TO_t_driver"
        -- 센터 -> 기사
        FOREIGN KEY (
                     "center_seq" -- 센터시퀀스
            )
            REFERENCES "t_center" ( -- 센터
                                   "center_seq" -- 센터시퀀스
                );

-- 센터 -> 기사
COMMENT ON CONSTRAINT "FK_t_center_TO_t_driver" ON "t_driver" IS '센터 -> 기사';

-- 배송
ALTER TABLE "t_delivery"
    ADD CONSTRAINT "FK_t_dispatch_TO_t_delivery"
        -- 배차 -> 배송
        FOREIGN KEY (
                     "dispatch_seq" -- 배차시퀀스
            )
            REFERENCES "t_dispatch" ( -- 배차
                                     "dispatch_seq" -- 배차시퀀스
                );

-- 배차 -> 배송
COMMENT ON CONSTRAINT "FK_t_dispatch_TO_t_delivery" ON "t_delivery" IS '배차 -> 배송';

-- 배송
ALTER TABLE "t_delivery"
    ADD CONSTRAINT "FK_t_vehicle_TO_t_delivery"
        -- 차량 -> 배송
        FOREIGN KEY (
                     "vehicle_seq" -- 차량시퀀스
            )
            REFERENCES "t_vehicle" ( -- 차량
                                    "vehicle_seq" -- 차량시퀀스
                );

-- 차량 -> 배송
COMMENT ON CONSTRAINT "FK_t_vehicle_TO_t_delivery" ON "t_delivery" IS '차량 -> 배송';

-- 배송
ALTER TABLE "t_delivery"
    ADD CONSTRAINT "FK_t_region_TO_t_delivery"
        -- 권역 -> 배송
        FOREIGN KEY (
                     "region_seq" -- 권역시퀀스
            )
            REFERENCES "t_region" ( -- 권역
                                   "region_seq" -- 권역시퀀스
                );

-- 권역 -> 배송
COMMENT ON CONSTRAINT "FK_t_region_TO_t_delivery" ON "t_delivery" IS '권역 -> 배송';

-- 배송
ALTER TABLE "t_delivery"
    ADD CONSTRAINT "FK_t_driver_TO_t_delivery"
        -- 기사 -> 배송
        FOREIGN KEY (
                     "driver_seq" -- 기사시퀀스
            )
            REFERENCES "t_driver" ( -- 기사
                                   "driver_seq" -- 기사시퀀스
                );

-- 기사 -> 배송
COMMENT ON CONSTRAINT "FK_t_driver_TO_t_delivery" ON "t_delivery" IS '기사 -> 배송';

-- 배송
ALTER TABLE "t_delivery"
    ADD CONSTRAINT "FK_t_attach_TO_t_delivery"
        -- 첨부 -> 배송
        FOREIGN KEY (
                     "delivery_photo_attach_seq" -- 배송사진첨부시퀀스
            )
            REFERENCES "t_attach" ( -- 첨부
                                   "attach_seq" -- 첨부시퀀스
                );

-- 첨부 -> 배송
COMMENT ON CONSTRAINT "FK_t_attach_TO_t_delivery" ON "t_delivery" IS '첨부 -> 배송';

-- 배차
ALTER TABLE "t_dispatch"
    ADD CONSTRAINT "FK_t_center_TO_t_dispatch"
        -- 센터 -> 배차
        FOREIGN KEY (
                     "center_seq" -- 센터시퀀스
            )
            REFERENCES "t_center" ( -- 센터
                                   "center_seq" -- 센터시퀀스
                );

-- 센터 -> 배차
COMMENT ON CONSTRAINT "FK_t_center_TO_t_dispatch" ON "t_dispatch" IS '센터 -> 배차';

-- 공통코드
ALTER TABLE "t_common_code"
    ADD CONSTRAINT "FK_t_common_code_group_TO_t_common_code"
        -- 공통코드그룹 -> 공통코드
        FOREIGN KEY (
                     "com_code_group_cd" -- 공통코드그룹코드
            )
            REFERENCES "t_common_code_group" ( -- 공통코드그룹
                                              "com_code_group_cd" -- 공통코드그룹코드
                );

-- 공통코드그룹 -> 공통코드
COMMENT ON CONSTRAINT "FK_t_common_code_group_TO_t_common_code" ON "t_common_code" IS '공통코드그룹 -> 공통코드';

-- 권역범위
ALTER TABLE "t_region_range"
    ADD CONSTRAINT "FK_t_region_TO_t_region_range"
        -- 권역 -> 권역범위
        FOREIGN KEY (
                     "region_seq" -- 권역시퀀스
            )
            REFERENCES "t_region" ( -- 권역
                                   "region_seq" -- 권역시퀀스
                );

-- 권역 -> 권역범위
COMMENT ON CONSTRAINT "FK_t_region_TO_t_region_range" ON "t_region_range" IS '권역 -> 권역범위';

-- 배송이력
ALTER TABLE "t_delivery_history"
    ADD CONSTRAINT "FK_t_delivery_TO_t_delivery_history"
        -- 배송 -> 배송이력
        FOREIGN KEY (
                     "delivery_seq" -- 배송시퀀스
            )
            REFERENCES "t_delivery" ( -- 배송
                                     "delivery_seq" -- 배송시퀀스
                );

-- 배송 -> 배송이력
COMMENT ON CONSTRAINT "FK_t_delivery_TO_t_delivery_history" ON "t_delivery_history" IS '배송 -> 배송이력';

-- 파일
ALTER TABLE "t_file"
    ADD CONSTRAINT "FK_t_attach_TO_t_file"
        -- 첨부 -> 파일
        FOREIGN KEY (
                     "attach_seq" -- 첨부시퀀스
            )
            REFERENCES "t_attach" ( -- 첨부
                                   "attach_seq" -- 첨부시퀀스
                );

-- 첨부 -> 파일
COMMENT ON CONSTRAINT "FK_t_attach_TO_t_file" ON "t_file" IS '첨부 -> 파일';