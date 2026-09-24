package com.obigo.microev.tms.admin.service.impl;


import com.obigo.microev.tms.admin.converter.AdminConverter;
import com.obigo.microev.tms.admin.presentation.admin.CreateAdminReqDto;
import com.obigo.microev.tms.admin.presentation.admin.ModifyAdminReqDto;
import com.obigo.microev.tms.admin.presentation.admin.SearchAdminsReqDto;
import com.obigo.microev.tms.admin.presentation.admin.SearchAdminsResDto;
import com.obigo.microev.tms.admin.service.AdminService;
import com.obigo.microev.tms.core.domain.entity.Admin;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import com.obigo.microev.tms.core.domain.mapper.AdminMapper;
import com.obigo.microev.tms.core.domain.mapper.vo.admin.SearchAdminsCondition;
import com.obigo.microev.tms.core.domain.mapper.vo.admin.SearchAdminsResult;
import com.obigo.microev.tms.core.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminServiceImpl implements AdminService {
    private final AdminMapper adminMapper;
    private final AdminConverter adminConverter;
    private final PasswordEncoder passwordEncoder;

    /**
     * 관리자 검색 조회
     * @param reqDto
     * @return
     */
    @Override
    public List<SearchAdminsResDto> searchAdmins(SearchAdminsReqDto reqDto) {
        SearchAdminsCondition condition = adminConverter.toSearchAdminsCondition(reqDto);
        List<SearchAdminsResult> adminsResults = adminMapper.findByConditions(condition);
        return adminConverter.toSearchAdminsResDtos(adminsResults);
    }

    /**
     * 관리자 생성
     * @param reqDto
     * @return
     */
    @Override
    public long createAdmin(CreateAdminReqDto reqDto) {
        //Email 중복 체크
        adminMapper.findByEmail(reqDto.getEmail()).ifPresent(admin -> {
            throw new InvalidRequestException(ResponseCode.DUPLICATED_EMAIL);
        });
        
        Admin admin = adminConverter.toAdmin(reqDto);

        //비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodedPassword);


        // insert 관리자
        adminMapper.insert(admin);

        return admin.getAdminSeq();
    }


    /**
     * 관리자 수정
     * @param adminSeq
     * @param reqDto
     */
    @Override
    public void modifyAdmin(Long adminSeq, ModifyAdminReqDto reqDto) {
        Admin admin = adminMapper.findById(adminSeq)
                .orElseThrow(() -> new InvalidRequestException(ResponseCode.NOT_FOUND_ADMIN));

        // update 관리자
        adminConverter.updateAdmin(admin, reqDto);
        adminMapper.update(admin);
    }
}
