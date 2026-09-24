package com.obigo.microev.tms.core.domain.mapper;

import com.obigo.microev.tms.core.domain.entity.Vehicle;
import com.obigo.microev.tms.core.domain.mapper.vo.vehicle.BelongingVehicleResult;
import com.obigo.microev.tms.core.domain.mapper.vo.vehicle.SearchVehiclesCondition;
import com.obigo.microev.tms.core.domain.mapper.vo.vehicle.SearchVehiclesResult;
import com.obigo.microev.tms.core.domain.mapper.vo.vehicle.VehicleByEupMyeonDongResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface VehicleMapper {
    long insert(Vehicle vehicle);

    long update(Vehicle vehicle);

    long deleteById(Long vehicleSeq);

    Optional<Vehicle> findById(Long vehicleSeq);

    Optional<Vehicle> findByVehicleNum(String vehicleNum);

    Optional<Vehicle> findByVehicleRegNum(String vehicleRegNum);

    Optional<Vehicle> findByDriverSeq(Long driverSeq);

    List<SearchVehiclesResult> findByConditions(SearchVehiclesCondition condition);

    List<BelongingVehicleResult> findByRegionSeq(Long regionSeq);

    List<BelongingVehicleResult> findByCenterSeq(Long centerSeq);

    Optional<VehicleByEupMyeonDongResult> findByCenterSeqAndEupMyeonDong(
        @Param("centerSeq") Long centerSeq,
        @Param("eupMyeonDong") String eupMyeonDong
    );
}
