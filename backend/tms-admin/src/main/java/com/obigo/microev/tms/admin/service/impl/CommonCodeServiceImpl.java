package com.obigo.microev.tms.admin.service.impl;

import com.obigo.microev.tms.admin.converter.CommonCodeConverter;
import com.obigo.microev.tms.admin.presentation.commonCode.*;
import com.obigo.microev.tms.admin.service.CommonCodeService;
import com.obigo.microev.tms.core.domain.entity.CommonCode;
import com.obigo.microev.tms.core.domain.entity.CommonCodeGroup;
import com.obigo.microev.tms.core.domain.enumeration.ResponseCode;
import com.obigo.microev.tms.core.domain.mapper.CommonCodeGroupMapper;
import com.obigo.microev.tms.core.domain.mapper.CommonCodeMapper;
import com.obigo.microev.tms.core.domain.mapper.vo.commonCode.SearchCommonCodeGroupsCondition;
import com.obigo.microev.tms.core.domain.mapper.vo.commonCode.SearchCommonCodeGroupsResult;
import com.obigo.microev.tms.core.domain.mapper.vo.commonCode.ValidCommonCodesResult;
import com.obigo.microev.tms.core.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class CommonCodeServiceImpl implements CommonCodeService {

    private final CommonCodeGroupMapper commonCodeGroupMapper;
    private final CommonCodeMapper commonCodeMapper;

    private final CommonCodeConverter commonCodeConverter;


    /**
     * 유효한 공통코드 목록 조회
     * @param comCodeGroupCd
     * @return
     */
    @Override
    public List<GetValidCommonCodesResDto> getValidCommonCodes(String comCodeGroupCd) {
        List<ValidCommonCodesResult> validCodes = commonCodeMapper.findValidCodes(comCodeGroupCd, null);
        return commonCodeConverter.toGetValidCommonCodesResDto(validCodes);
    }

    /**
     * 그룹코드 목록 조회
     * @param reqDto
     * @return
     */
    @Override
    public List<SearchCommonCodeGroupsResDto> searchCommonCodeGroups(SearchCommonCodeGroupsReqDto reqDto) {
        SearchCommonCodeGroupsCondition condition = commonCodeConverter.toSearchCommonCodeGroupsCondition(reqDto);
        List<SearchCommonCodeGroupsResult> searchCommonCodeGroupsResults = commonCodeGroupMapper.searchCommonCodeGroups(condition);
        return commonCodeConverter.toSearchCommonCodeGroupsResDto(searchCommonCodeGroupsResults);
    }


    /**
     * 공통코드 그룹 생성
     * @param reqDto
     * @return
     */
    @Override
    public CreateCommonCodeGroupResDto createCommonCodeGroup(CreateCommonCodeGroupReqDto reqDto) {

        //그룹코드 중복 확인
        Boolean res = commonCodeGroupMapper.findById(reqDto.getComCodeGroupCd()).isPresent();
        if (res.equals(true)){
            throw new InvalidRequestException(ResponseCode.DUPLICATED_COMMON_CODE_GROUP);
        }

        CommonCodeGroup commonCodeGroup = commonCodeConverter.toCommonCodeGroup(reqDto);
        commonCodeGroupMapper.insertCommonCodeGroup(commonCodeGroup);

        return CreateCommonCodeGroupResDto.builder()
                .comCodeGroupCd(commonCodeGroup.getComCodeGroupCd())
                .build();
    }

    /**
     * 공통코드 그룹 수정
     * @param reqDto
     * @return
     */
    @Override
    public ModifyCommonCodeGroupResDto modifyCommonCodeGroup(ModifyCommonCodeGroupReqDto reqDto) {
        String comCodeGroupCd = reqDto.getComCodeGroupCd();

        //공통코드그룹 존재하는지 체크
        CommonCodeGroup commonCodeGroup = commonCodeGroupMapper.findById(comCodeGroupCd)
                .orElseThrow(() -> new InvalidRequestException(ResponseCode.NOT_FOUND_COMMON_CODE_GROUP));

        //그룹정보 Update
        commonCodeConverter.updateCommonCodeGroup(commonCodeGroup, reqDto);
        commonCodeGroupMapper.updateCommonCodeGroup(commonCodeGroup);

        return ModifyCommonCodeGroupResDto.builder()
                .comCodeGroupCd(commonCodeGroup.getComCodeGroupCd())
                .comCodeGroupName(commonCodeGroup.getComCodeGroupName())
                .build();
    }

    /**
     * 공통코드 그룹 삭제
     * @param reqDto
     */
    @Override
    public void deleteCommonCodeGroups(DeleteCommonCodeGroupReqDto reqDto) {

        //하위에 존재하는 공통코드가 있는지 체크
        reqDto.getComCodeGroupCds().forEach(comCodeGroupCd -> {
            boolean existsCommonCode = !commonCodeMapper.findByComCodeGroupCd(comCodeGroupCd).isEmpty();
            if (existsCommonCode) {
                throw new InvalidRequestException(ResponseCode.DELETE_EXISTS_COMMON_CODE);
            }
        });

        //해당 그룹코드 삭제
        reqDto.getComCodeGroupCds().forEach(
                comCodeGroupCd -> {
                    commonCodeGroupMapper.findById(comCodeGroupCd)
                            .orElseThrow(() -> new InvalidRequestException(ResponseCode.NOT_FOUND_COMMON_CODE_GROUP));
                    commonCodeGroupMapper.deleteById(comCodeGroupCd);
                }
        );
    }

    /**
     * 공통코드 전체 목록 조회
     * @param comCodeGroupCd
     * @return
     */
    @Override
    public List<GetCommonCodesResDto> getCommonCodes(String comCodeGroupCd) {
        List<CommonCode> commonCodes = commonCodeMapper.findByComCodeGroupCd(comCodeGroupCd);
        return commonCodeConverter.toGetCommonCodesResDto(commonCodes);
    }

    /**
     * 공통코드 생성
     * @param reqDto
     * @return
     */
    @Override
    public Long createCommonCodes(CreateCommonCodeReqDto reqDto) {
        String comCodeGroupCd = reqDto.getComCodeGroupCd();
        String comCodeCd = reqDto.getComCodeCd();

        //공통코드그룹 존재하는지 체크
        commonCodeGroupMapper.findById(comCodeGroupCd)
                .orElseThrow(() -> new InvalidRequestException(ResponseCode.NOT_FOUND_COMMON_CODE_GROUP));

        //공통코드 중복 확인
        boolean anyMatch = commonCodeMapper.findByComCodeGroupCd(comCodeGroupCd).stream()
                .anyMatch(c -> c.getComCodeCd().equals(comCodeCd));
        if (anyMatch) {
            throw new InvalidRequestException(ResponseCode.DUPLICATED_COMMON_CODE);
        }

        //공통코드 Insert
        CommonCode commonCode = commonCodeConverter.toCommonCode(reqDto);
        commonCodeMapper.insertCommonCode(commonCode);
        return commonCode.getComCodeSeq();
    }


    /**
     * 공통코드 수정
     * @param comCodeSeq
     * @param reqDto
     * @return
     */
    @Override
    public void modifyCommonCodes(Long comCodeSeq, ModifyCommonCodeReqDto reqDto) {
        //공통코드 존재하는지 체크
        CommonCode commonCode = commonCodeMapper.findById(comCodeSeq)
                .orElseThrow(() -> new InvalidRequestException(ResponseCode.NOT_FOUND_COMMON_CODE));

        //공통코드 Update
        commonCodeConverter.updateCommonCode(commonCode, reqDto);
        commonCodeMapper.updateCommonCode(commonCode);
    }



    /**
     * 공통코드 그룹 삭제
     * @param reqDto
     */
    @Override
    public void deleteCommonCodes(DeleteCommonCodeReqDto reqDto) {

        //공통코드 존재하는지 체크
        reqDto.getComCodeSeqs().forEach(comCodeSeq -> {
            CommonCode commonCode = commonCodeMapper.findById(comCodeSeq)
                    .orElseThrow(() -> new InvalidRequestException(ResponseCode.NOT_FOUND_COMMON_CODE));
        });

        //해당 공통코드 삭제
        reqDto.getComCodeSeqs().forEach(commonCodeMapper::deleteById);
    }

}
