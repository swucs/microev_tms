package com.obigo.microev.tms.admin.presentation.vehicle;

import com.obigo.microev.tms.core.domain.validator.common.ValidCommonCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateVehicleReqDto {
    @Schema(description = "차량명", example = "차량명")
    @NotBlank(message = "{validation.vehicle.vehicleName.notBlank}")
    private String vehicleName;

    @Schema(description = "차종", example = "포터2")
    @NotBlank(message = "{validation.vehicle.model.notBlank}")
    private String model;

    @Schema(description = "센터시퀀스")
    @NotNull(message = "{validation.vehicle.centerSeq.notNull}")
    private Long centerSeq;

    @Schema(description = "기사시퀀스")
//    @NotNull(message = "{validation.vehicle.driverSeq.notNull}")
    private Long driverSeq;

    @Schema(description = "권역시퀀스")
    private Long regionSeq;
    
    @Schema(description = "차량유형코드", example = "CARGO", allowableValues = {"CARGO", "BOX_TRUCK", "WING_BODY"})
    @NotBlank(message = "{validation.vehicle.vehicleTypeCd.notBlank}")
    @ValidCommonCode(groupCode = "VehicleType")
    private String vehicleTypeCd;

    @Schema(description = "차량번호", example = "123가1234")
    @NotBlank(message = "{validation.vehicle.vehicleNum.notBlank}")
    private String vehicleNum;

    @Schema(description = "차량등록번호")
    @NotBlank(message = "{validation.vehicle.vehicleRegNum.notBlank}")
    private String vehicleRegNum;

    @Schema(description = "연식", example = "2021")
    @NotBlank(message = "{validation.vehicle.modelYear.notBlank}")
    private String modelYear;

    @Schema(description = "차량등록지", example = "서울")
    private String vehicleRegArea;

    @Schema(description = "차량 소유자명", example = "홍길동")
    @NotBlank(message = "{validation.vehicle.ownerName.notBlank}")
    private String ownerName;

    @Schema(description = "길이", example = "5100")
    @NotBlank(message = "{validation.vehicle.length.notBlank}")
    private String length;
    
    @Schema(description = "넓이", example = "1800")
    @NotBlank(message = "{validation.vehicle.width.notBlank}")
    private String width;

    @Schema(description = "톤급", example = "10 Ton")
    @NotBlank(message = "{validation.vehicle.tonGrade.notBlank}")
    private String tonGrade;

    @Schema(description = "최대적재량", example = "1500Kg")
    @NotBlank(message = "{validation.vehicle.maxLoadingCapacity.notBlank}")
    private String maxLoadingCapacity;
    
    @Schema(description = "연료구분코드", example = "DIESEL", allowableValues = {"DIESEL", "GASOLINE", "LPG", "ELECTRIC"})
    @NotBlank(message = "{validation.vehicle.fuelTypeCd.notBlank}")
    @ValidCommonCode(groupCode = "FuelType")
    private String fuelTypeCd;

    @Schema(description = "연비", example = "10.5Km/ℓ")
    @NotBlank(message = "{validation.vehicle.fuelEfficiency.notBlank}")
    private String fuelEfficiency;

    @Schema(description = "자용구분코드", example = "RENTED", allowableValues = {"RENTED", "OWN"})
    @NotBlank(message = "{validation.vehicle.vehicleUseTypeCd.notBlank}")
    @ValidCommonCode(groupCode = "VehicleUseType")
    private String vehicleUseTypeCd;
    
    @Schema(description = "차고지명", example = "차고지명")
    @NotBlank(message = "{validation.vehicle.garageName.notBlank}")
    private String garageName;

    @Schema(description = "사용여부", example = "Y", allowableValues = {"Y", "N"})
    @NotBlank(message = "{validation.vehicle.usageYn.notBlank}")
    private String usageYn;

}
