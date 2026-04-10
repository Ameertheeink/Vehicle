package com.vehicle.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class PollutionRequestDTO {
    private Long vehicleId;
    private String certificateNumber;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private Double cost;
    private String vendor;
}