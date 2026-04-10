package com.vehicle.demo.repository;

import com.vehicle.demo.entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {

    List<Insurance> findByVehicleId(Long vehicleId);

    Insurance findTopByVehicleIdOrderByExpiryDateDesc(Long vehicleId);

}