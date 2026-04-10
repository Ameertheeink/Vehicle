package com.vehicle.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
public class PollutionResponseDTO {
    private Long id;
    private Long vehicleId;
    private String certificateNumber;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private Double cost;
    private String vendor;

    private String documentName;
    private String documentType;

    private LocalDateTime createdAt;
}