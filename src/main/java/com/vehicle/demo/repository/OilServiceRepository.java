package com.vehicle.demo.repository;

import com.vehicle.demo.entity.OilService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OilServiceRepository extends JpaRepository<OilService, Long> {

    List<OilService> findByVehicleId(Long vehicleId);

}
