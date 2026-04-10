package com.vehicle.demo.repository;

import com.vehicle.demo.entity.VehicleImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleImageRepository extends JpaRepository<VehicleImage, Long> {

    List<VehicleImage> findByVehicleId(Long vehicleId);
    void deleteByVehicleId(Long vehicleId);

}
