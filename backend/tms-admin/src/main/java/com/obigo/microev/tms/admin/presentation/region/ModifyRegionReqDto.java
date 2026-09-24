package com.obigo.microev.tms.admin.presentation.region;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ModifyRegionReqDto {
    @Schema(description = "권역명")
    @NotBlank(message = "{validation.region.regionName.notBlank}")
    private String regionName;

    @Schema(description = "읍면동 리스트", example = "경기도 성남시 분당구 판교동")
    @NotNull
    @Size(min = 1, message = "{validation.region.eupMyeonDongs.minSize}")
    private List<String> eupMyeonDongs;
}
