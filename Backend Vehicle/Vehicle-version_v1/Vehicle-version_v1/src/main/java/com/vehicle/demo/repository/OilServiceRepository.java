package com.vehicle.demo.repository;

import com.vehicle.demo.entity.OilService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OilServiceRepository extends JpaRepository<OilService, Long> {

    List<OilService> findByVehicleId(Long vehicleId);

    OilService findTopByVehicleIdOrderByLastServiceDateDesc(Long vehicleId);

    Page<OilService> findByVehicleId(Long vehicleId, Pageable pageable);
}