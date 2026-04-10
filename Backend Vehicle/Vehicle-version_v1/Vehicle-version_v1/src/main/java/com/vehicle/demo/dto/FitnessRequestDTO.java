package com.vehicle.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FitnessRequestDTO {
    private Long vehicleId;
    private LocalDate issueDate;
    private LocalDate expiryDate;
}