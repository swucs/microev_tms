# AGENTS.md

- 작업 범위는 `backend/`의 티엠에스만. `frontend/`는 대상 제외. 아래 모든 경로는 `backend/` 기준.

## 저장소 구조
- `backend/`: 그레이들 루트 `tms` 전체 (이전 루트 내용 그대로 이동, 이력 유지).
- `frontend/`: 비어 있음. 백엔드 작업 시 건드리지 말 것.
- 명령은 `backend/` 안에서 실행. 래퍼에 실행 비트가 없어 `./gradlew` 대신 `sh gradlew` 사용.

## 모듈 경계
- 루트 `settings.gradle` 포함 모듈만 빌드 대상: `:tms-admin`, `:tms-api`, `:tms-core`(경로 `tms-library/tms-core`), `:lib-redis`.
- 자바 21 고정, 스프링 부트 3.2.0. 버전 통일·업그레이드 임의 시도 금지.

## 실행·빌드 명령
- `tms-admin` 실행: `JASYPT_KEY=TEST_KEY sh gradlew :tms-admin:bootRun --args='--spring.profiles.active=local'` (`tms-api`도 동일 패턴).
- 모듈 빌드: `sh gradlew :tms-api:build`, `sh gradlew :tms-admin:build`, `sh gradlew :tms-core:build`.
- 단일 테스트: `sh gradlew :tms-admin:test --tests "com.obigo.microev.tms.admin.service.XxxTest"`. 현재 `src/test`가 없어서 실패 시 신규 작성 여부 먼저 확인.
- 로컬 디비는 도커 `tms-postgres`(`localhost:15432/tms_dev_db`). 기동·종료: `docker start tms-postgres` / `docker stop tms-postgres`.

## 포트·프로파일
- `tms-admin`: 8090, 경로 `/tms/admin` / `tms-api`: 8091, 경로 `/tms/api`.
- 기본 활성 프로파일은 `local`.

## 설정 수정 위치
- `tms-admin`, `tms-api`의 `application.yml`은 포트·경로와 `spring.config.import`만 담당. 실제 값은 `tms-library/*/src/main/resources/application-*.yml` 원본에서 수정.

## 복호화·외부 의존성
- `ENC(...)` 값은 실행 시 복호화되며 키는 환경변수 `JASYPT_KEY`에서 읽음(`tms-core/.../config/JasyptConfig.java:14`). 로컬 키는 `TEST_KEY`.
- 보안 규칙: 평문 비번·키는 소스·설정·스크립트·엠디 어디에도 기록 금지, 전달은 환경변수로만. 암호화는 `PBEWithMD5AndDES`·반복 1000 (`JasyptConfig.java:14-26`과 동일 조건).
- `local` 블록만 로컬 값으로 교체했고 `dev` 블록은 원본 유지. `dev` 값을 건드리면 운영 키 복호화가 깨지므로 금지.
- 로컬 더미 주의: 에스쓰리·카카오 `local` 키는 부팅용 임시값이라 로컬에서 사진 업로드·주소 검색은 실패함. 디비·로그인은 정상 동작. 엠큐티는 제거됨(배송상태 알림은 `tms-api` 내부 SSE 직접 발행).

## 데이터베이스
- 포스트그레스 전용(`database/createTable.sql:4` `BIGSERIAL`, `:32` `COMMENT ON`, `:589` `NULLS LAST`). 테이블 14개, 접두 `t_`, 기본키 `*_seq`, 외래키는 파일 하단(`createTable.sql:1212-1406`)에 모음.
- 핵심 흐름: `t_center` → `t_dispatch`(배송일자·배차상태) → `t_delivery`(`dispatch_seq` 필수, `tracking_num` 유일) → `t_delivery_history`. `t_delivery`는 차량·기사·권역·첨부를 함께 참조.
- 기준 정보: `t_center`·`t_region`(`t_region_range` 1대다)·`t_driver`(센터 종속)·`t_vehicle`(센터·권역·기사 참조)·`t_admin`(독립). 파일은 `t_attach` → `t_file`, 호출 기록은 `t_api_call`.
- 상태·유형 코드(`*_cd`)는 외래키 없이 문자열로 저장하고 앱 검증기로 검사. 코드값 추가는 `t_common_code` 행 추가가 정답이며 컬럼·이넘만 바꾸면 검증 불일치 발생. 초기값은 `database/initData.sql:8-71`, 표본은 `database/testData.sql` 사용.
- 투입 순서: `createTable.sql → initData.sql → (선택) testData.sql`.

## 데이터 접근 규칙
- 마이바티스 전용: 인터페이스 `tms-core/.../domain/mapper`, 실행문 `tms-core/src/main/resources/mappers/*.xml`, 설정 `mybatis/mybatis-config.xml`, 스캔 `tms-core/.../config/MybatisConfig.java:7`. 제이피에이 어노테이션·리포지토리 추가 금지.
- 의존 방향 고정: `tms-admin`은 `:tms-core`만, `tms-api`는 `:tms-core`·`:lib-redis` 참조. `tms-admin`에 레디스 의존 추가 금지.
- 계층 순서: `presentation`(컨트롤러·요청·응답) → `service` → `tms-core` 매퍼. 변환기는 맵스트럭트·롬복 애노테이션 프로세서로 생성되므로 객체 변경 후 해당 모듈 리빌드 필요.

## 수동 검증
- `http/admin/*.http`, `http/api/*.http`와 `http/http-client.env.json` 조합 사용. `local`은 `localhost:8090`·`8091`, `prod`는 `evcar-tms.obigo.com` 기준.
- 스웨거 스캔 범위가 모듈별로 고정됨(`application-local.yml`, `application-dev.yml`의 `packages-to-scan`). 컨트롤러를 범위 밖 패키지에 두면 문서에 노출 안 됨.
- 배달 구독 SSE(`/tms/api/delivery/subscribe`)는 `nginx/default.conf:36-48`에서 버퍼링 해제로 별도 처리됨. 일반 프록시 설정으로 검증하지 말 것.

## 저장소에 없는 것
- `src/test`, 검사·포맷·타입체크 설정, 씨아이 워크플로 없음. 없는 명령·설정을 추정해서 만들지 말고 필요 시 사용자에게 확인.
