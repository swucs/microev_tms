package com.obigo.microev.tms.core.domain.mapper;

import com.obigo.microev.tms.core.domain.entity.Admin;
import com.obigo.microev.tms.core.domain.mapper.vo.admin.SearchAdminsCondition;
import com.obigo.microev.tms.core.domain.mapper.vo.admin.SearchAdminsResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AdminMapper {
    Optional<Admin> findById(Long adminSeq);

    Optional<Admin> findByEmail(String email);

    void updateAccessInfo(Admin admin);

    void updateRefreshToken(Admin admin);

    List<SearchAdminsResult> findByConditions(SearchAdminsCondition searchAdminsCondition);

    long insert(Admin admin);

    long update(Admin admin);
}
