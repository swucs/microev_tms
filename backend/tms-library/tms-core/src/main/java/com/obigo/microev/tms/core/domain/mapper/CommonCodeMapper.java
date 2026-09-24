package com.obigo.microev.tms.core.domain.mapper;

import com.obigo.microev.tms.core.domain.entity.CommonCode;
import com.obigo.microev.tms.core.domain.mapper.vo.commonCode.ValidCommonCodesResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;


@Mapper
public interface CommonCodeMapper {
    List<ValidCommonCodesResult> findValidCodes(
            @Param("comCodeGroupCd") String comCodeGroupCd
            , @Param("comCodeCd") String comCodeCd
    );

    List<CommonCode> findByComCodeGroupCd(String comCodeGroupCd);

    Optional<CommonCode> findById(Long comCodeSeq);

    long insertCommonCode(CommonCode commonCode);

    void updateCommonCode(CommonCode commonCode);

    void deleteById(Long comCodeSeq);

}
