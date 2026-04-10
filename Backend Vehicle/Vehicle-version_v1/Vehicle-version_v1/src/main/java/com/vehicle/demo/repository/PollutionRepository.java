package com.vehicle.demo.repository;

import com.vehicle.demo.entity.PollutionCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollutionRepository extends JpaRepository<PollutionCertificate, Long> {

    List<PollutionCertificate> findByVehicleId(Long vehicleId);

    PollutionCertificate findTopByVehicleIdOrderByExpiryDateDesc(Long vehicleId);
}