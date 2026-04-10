package com.vehicle.demo.repository;

import com.vehicle.demo.entity.FitnessCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FitnessCertificateRepository
        extends JpaRepository<FitnessCertificate, Long> {

    Optional<FitnessCertificate> findByVehicleId(Long vehicleId);


}