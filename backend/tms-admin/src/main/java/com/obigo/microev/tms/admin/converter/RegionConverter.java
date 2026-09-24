package com.obigo.microev.tms.admin.converter;

import com.obigo.microev.tms.admin.presentation.region.*;
import com.obigo.microev.tms.admin.utils.AuthenticationUtils;
import com.obigo.microev.tms.core.domain.entity.Region;
import com.obigo.microev.tms.core.domain.entity.RegionRange;
import com.obigo.microev.tms.core.domain.mapper.vo.region.RegionsFindByConditionsResult;
import com.obigo.microev.tms.core.domain.mapper.vo.region.SearchRegionsCondition;
import com.obigo.microev.tms.core.domain.mapper.vo.vehicle.BelongingVehicleResult;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE
        , imports = {LocalDateTime.class, AuthenticationUtils.class}
)
public interface RegionConverter {

    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "creatorSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    Region toRegion(CreateRegionReqDto createRegionReqDto);


    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifierSeq", expression = "java(AuthenticationUtils.getCurrentAdmin().getAdminSeq())")
    void updateRegion(@MappingTarget Region region, ModifyRegionReqDto modifyRegionReqDto);

    SearchRegionsCondition toSearchRegionsCondition(SearchRegionsReqDto reqDto);

    SearchRegionsResDto toSearchRegionsResDto(RegionsFindByConditionsResult regionsFindByConditionsResult);

    RegionDetailResDto toRegionDetailResDto(Region region);

    List<RegionDetailResDto.Vehicle> toVehicles(List<BelongingVehicleResult> belongingVehicleResults);


    default List<RegionRange> toRegionRanges(CreateRegionReqDto reqDto, Long regionSeq) {
        if (reqDto.getEupMyeonDongs() == null) {
            return List.of();
        }

        return reqDto.getEupMyeonDongs().stream().map(eupMyeonDong ->
            RegionRange.builder()
                .regionSeq(regionSeq)
                .eupMyeonDong(eupMyeonDong)
                .build()
        ).toList();
    }

    default List<RegionRange> toRegionRanges(ModifyRegionReqDto reqDto, Long regionSeq) {
        if (reqDto.getEupMyeonDongs() == null) {
            return List.of();
        }

        return reqDto.getEupMyeonDongs().stream().map(eupMyeonDong ->
                RegionRange.builder()
                        .regionSeq(regionSeq)
                        .eupMyeonDong(eupMyeonDong)
                        .build()
        ).toList();
    }
}
