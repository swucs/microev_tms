package com.obigo.microev.tms.core.domain.mapper;

import com.obigo.microev.tms.core.domain.entity.CommonCodeGroup;
import com.obigo.microev.tms.core.domain.mapper.vo.commonCode.SearchCommonCodeGroupsCondition;
import com.obigo.microev.tms.core.domain.mapper.vo.commonCode.SearchCommonCodeGroupsResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;


@Mapper
public interface CommonCodeGroupMapper {

    List<SearchCommonCodeGroupsResult> searchCommonCodeGroups(SearchCommonCodeGroupsCondition condition);

    Optional<CommonCodeGroup> findById(String comCodeGroupCd);

    void insertCommonCodeGroup(CommonCodeGroup commonCodeGroup);

    void updateCommonCodeGroup(CommonCodeGroup commonCodeGroup);

    void deleteById(String comCodeGroupCd);
}
