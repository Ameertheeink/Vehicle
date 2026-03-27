package com.vehicle.demo.repository;

import com.vehicle.demo.entity.TyreService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TyreServiceRepository extends JpaRepository<TyreService, Long> {

    List<TyreService> findByVehicleId(Long vehicleId);

    TyreService findTopByVehicleIdOrderByChangeDateDesc(Long vehicleId);



}