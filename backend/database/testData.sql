INSERT INTO public.t_center
(center_name, center_type_cd, center_addr_1, center_addr_2, zip_code, latitude, longitude, total_area, manager_name, contact_num, usage_yn, created_at, creator_seq, modified_at, modifier_seq)
VALUES('판교 롯데마트', 'SUPERMARKET', '경기 성남시 분당구 대왕판교로606번길', '58', '13525', 37.3955577, 127.1133755, 1000.352, '김오비고1', '010-1234-5678', 'Y', '2024-07-25 12:05:13.543', 1, '2024-07-25 14:03:46.539', 1);
INSERT INTO public.t_center
(center_name, center_type_cd, center_addr_1, center_addr_2, zip_code, latitude, longitude, total_area, manager_name, contact_num, usage_yn, created_at, creator_seq, modified_at, modifier_seq)
VALUES('롯데택배 판교동원대리점', 'DELIVERY', '경기도 광주시 경충대로 2056', '롯데택배 판교동원대리점', '13465', 37.4060975, 127.2145405, 177.5, '김롯데', '031-752-4786', 'Y', '2024-07-25 14:15:59.800', 1, '2024-07-25 14:15:59.801', 1);


INSERT INTO public.t_driver (driver_name, center_seq, login_id, ext_system_linked_id, "password", driver_phone_num, email, driver_license_num, driver_license_date, driver_license_agency, working_days, working_start_hour, working_end_hour, status_cd, comp_name, created_at, creator_seq, modified_at, modifier_seq) VALUES
    ('홍길동',  (SELECT center_seq FROM t_center WHERE center_name='롯데택배 판교동원대리점')
    ,'driver01', 'lotte_driver01', '$2a$10$jdX4n.Jr7464BDaiZAdv4O8vuvCF20zmuXj.H4Osts2QLwrOK2QU6', '010-1234-5678', 'test@obigo.com', '11-02-33333', '2023-01-01', '운전면허 발급기관', '월화수목금', '09:00:00', '18:00:00', 'WORKING', '오비고', '2024-07-23 17:50:44.384', 1, '2024-07-23 17:50:44.384', 1);

INSERT INTO public.t_driver (driver_name, center_seq, login_id, ext_system_linked_id, "password", driver_phone_num, email, driver_license_num, driver_license_date, driver_license_agency, working_days, working_start_hour, working_end_hour, status_cd, comp_name, created_at, creator_seq, modified_at, modifier_seq) VALUES
    ('김기사',  (SELECT center_seq FROM t_center WHERE center_name='판교 롯데마트')
    , 'driver02', 'lotte_driver02', '$2a$10$jdX4n.Jr7464BDaiZAdv4O8vuvCF20zmuXj.H4Osts2QLwrOK2QU6', '010-1234-5678', 'test@obigo.com', '11-02-33333', '2023-01-01', '운전면허 발급기관', '월화수목금', '09:00:00', '18:00:00', 'WORKING', '오비고', '2024-07-29 16:02:17.326', 1, '2024-07-29 16:02:17.326', 1);


