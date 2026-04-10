package com.vehicle.demo.repository;

import com.vehicle.demo.entity.TyreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TyreServiceRepository extends JpaRepository<TyreService, Long> {

    List<TyreService> findByVehicleId(Long vehicleId);

    TyreService findTopByVehicleIdOrderByChangeDateDesc(Long vehicleId);


    TyreService findTopByVehicleIdOrderByCreatedAtDesc(Long vehicleId);

    Page<TyreService> findAll(Pageable pageable);

    Page<TyreService> findByVehicleId(Long vehicleId, Pageable pageable);
}