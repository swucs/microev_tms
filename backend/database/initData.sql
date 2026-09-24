INSERT INTO t_admin
(email, admin_name, "password", contact, "position", fail_count, password_changed_at, last_accessed_at
, refresh_token, status_cd, created_at, creator_seq, modified_at, modifier_seq)
VALUES('admin@obigo.com', '오비고', '$2a$10$S.nZKgGUDxxnPxMN8IMljun8ZO28dWjrEonpwHXIiRhF.CdNPDuLa', '010-1111-2222', '과장', 0, NULL, NULL
      , NULL, 'Normal', '2024-07-23 15:24:22.000', 1, '2024-07-23 15:24:22.000', 1);


insert into t_common_code_group (com_code_group_cd, com_code_group_name, created_at, creator_seq, modified_at,
                                 modifier_seq)
values
     ('AdminStatus', '관리자상태', NOW(), 0, NOW(), 0)
     , ('DriverStatus', '기사상태', NOW(), 0, NOW(), 0)
     , ('VehicleType', '차량유형', NOW(), 0, NOW(), 0)
     , ('FuelType', '연료구분', NOW(), 0, NOW(), 0)
     , ('VehicleUseType', '자용구분', NOW(), 0, NOW(), 0)
     , ('CenterType', '센터유형', NOW(), 0, NOW(), 0)
     , ('DispatchStatus', '배차상태', NOW(), 0, NOW(), 0)
     , ('DeliveryType', '배송유형', NOW(), 0, NOW(), 0)
     , ('DeliveryStatus', '배송상태', NOW(), 0, NOW(), 0)
     , ('ShootingImpossibleReason', '촬영불가사유', NOW(), 0, NOW(), 0)
     , ('ConsignmentLocation', '위탁장소', NOW(), 0, NOW(), 0)
     , ('UncompletedReason', '미배송(미수거)사유', NOW(), 0, NOW(), 0)
;


insert into t_common_code (com_code_group_cd, com_code_cd, com_code_name, sort_order, usage_yn,
                           created_at, creator_seq, modified_at, modifier_seq)
values
       ('AdminStatus', 'Normal', '정상', 1, 'Y', NOW(), 0, NOW(), 0)
     , ('AdminStatus', 'Dormancy', '휴면', 2, 'Y', NOW(), 0, NOW(), 0)
     , ('AdminStatus', 'Locked', '계정잠김', 3, 'Y', NOW(), 0, NOW(), 0)
     , ('AdminStatus', 'Disabled', '비활성', 4, 'Y', NOW(), 0, NOW(), 0)
     , ('DriverStatus', 'WORKING', '정상근무', 1, 'Y', NOW(), 0, NOW(), 0)
     , ('DriverStatus', 'ABSENCE', '휴직', 2, 'Y', NOW(), 0, NOW(), 0)
     , ('DriverStatus', 'RETIRED', '퇴사', 3, 'Y', NOW(), 0, NOW(), 0)
     , ('VehicleType', 'CARGO', '카고', 1, 'Y', NOW(), 0, NOW(), 0)
     , ('VehicleType', 'BOX_TRUCK', '탑차', 2, 'Y', NOW(), 0, NOW(), 0)
     , ('VehicleType', 'WING_BODY', '윙바디', 3, 'Y', NOW(), 0, NOW(), 0)
     , ('FuelType', 'LPG', 'LPG', 1, 'Y', NOW(), 0, NOW(), 0)
     , ('FuelType', 'GASOLINE', '휘발유', 2, 'Y', NOW(), 0, NOW(), 0)
     , ('FuelType', 'DIESEL', '경유', 3, 'Y', NOW(), 0, NOW(), 0)
     , ('FuelType', 'ELECTRIC', '전기', 4, 'Y', NOW(), 0, NOW(), 0)
     , ('VehicleUseType', 'RENTED', '지입', 1, 'Y', NOW(), 0, NOW(), 0)
     , ('VehicleUseType', 'OWN', '자차', 2, 'Y', NOW(), 0, NOW(), 0)
     , ('CenterType', 'DELIVERY', '택배', 1, 'Y', NOW(), 0, NOW(), 0)
     , ('CenterType', 'SUPERMARKET', '슈퍼', 2, 'Y', NOW(), 0, NOW(), 0)
     , ('DispatchStatus', 'DISPATCH_WAITING', '배차대기', 1, 'Y', NOW(), 0, NOW(), 0)
     , ('DispatchStatus', 'DISPATCHED', '배차확정', 2, 'Y', NOW(), 0, NOW(), 0)
     , ('DispatchStatus', 'INSPECTION_COMPLETED', '검수완료', 3, 'Y', NOW(), 0, NOW(), 0)
     , ('DispatchStatus', 'IN_DELIVERY', '배송중', 4, 'Y', NOW(), 0, NOW(), 0)
     , ('DispatchStatus', 'COMPLETED', '배송완료', 5, 'Y', NOW(), 0, NOW(), 0)
     , ('DeliveryType', 'DELIVERY', '배송', 1, 'Y', NOW(), 0, NOW(), 0)
     , ('DeliveryType', 'COLLECTION', '수거', 2, 'Y', NOW(), 0, NOW(), 0)
     , ('DeliveryStatus', 'INSPECTION_WAITING', '검수대기', 1, 'Y', NOW(), 0, NOW(), 0)
     , ('DeliveryStatus', 'INSPECTION_COMPLETED', '검수완료', 2, 'Y', NOW(), 0, NOW(), 0)
     , ('DeliveryStatus', 'IN_DELIVERY', '배송중', 3, 'Y', NOW(), 0, NOW(), 0)
     , ('DeliveryStatus', 'COMPLETED', '완료', 4, 'Y', NOW(), 0, NOW(), 0)
     , ('DeliveryStatus', 'UNCOMPLETED', '미완료', 5, 'Y', NOW(), 0, NOW(), 0)
     , ('ShootingImpossibleReason', 'BROKEN_CAMERA', '카메라 고장', 1, 'Y', NOW(), 0, NOW(), 0)
     , ('ShootingImpossibleReason', 'SERVER_ERROR', '서비스 오류', 2, 'Y', NOW(), 0, NOW(), 0)
     , ('ShootingImpossibleReason', 'ETC', '기타', 3, 'Y', NOW(), 0, NOW(), 0)
     , ('ConsignmentLocation', 'FRONT_OF_DOOR', '문 앞', 1, 'Y', NOW(), 0, NOW(), 0)
     , ('ConsignmentLocation', 'SECURITY_OFFICE', '경비실', 2, 'Y', NOW(), 0, NOW(), 0)
     , ('ConsignmentLocation', 'DELIVERY_BOX', '택배함', 3, 'Y', NOW(), 0, NOW(), 0)
     , ('ConsignmentLocation', 'ETC', '기타', 4, 'Y', NOW(), 0, NOW(), 0)
     , ('UncompletedReason', 'EXCESSIVE_QUANTITY', '물량 과다', 1, 'Y', NOW(), 0, NOW(), 0)
     , ('UncompletedReason', 'CUSTOMER_REQUEST', '고객 요청', 2, 'Y', NOW(), 0, NOW(), 0)
     , ('UncompletedReason', 'NO_ENTRY_BUILDING', '건물 진입 불가', 3, 'Y', NOW(), 0, NOW(), 0)
     , ('UncompletedReason', 'LOST_PARCEL', '택배물 분실', 4, 'Y', NOW(), 0, NOW(), 0)
     , ('UncompletedReason', 'ETC', '기타', 5, 'Y', NOW(), 0, NOW(), 0)
;