package com.vehicle.demo.repository;

import com.vehicle.demo.entity.VehicleRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRegistrationRepository
        extends JpaRepository<VehicleRegistration, Long> {

    List<VehicleRegistration> findByVehicleId(Long vehicleId);

    VehicleRegistration findTopByVehicleIdOrderByCreatedAtDesc(Long vehicleId);
}