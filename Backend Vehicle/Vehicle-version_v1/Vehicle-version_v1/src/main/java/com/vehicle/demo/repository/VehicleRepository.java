package com.vehicle.demo.repository;

import com.vehicle.demo.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByVehicleNumber(String vehicleNumber);
    Page<Vehicle> findAll(Pageable pageable);

    @Query("""
        SELECT v FROM Vehicle v
        WHERE LOWER(v.vehicleNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(v.ownerName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(v.brand) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(v.model) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<Vehicle> searchVehicles(String keyword, Pageable pageable);
}