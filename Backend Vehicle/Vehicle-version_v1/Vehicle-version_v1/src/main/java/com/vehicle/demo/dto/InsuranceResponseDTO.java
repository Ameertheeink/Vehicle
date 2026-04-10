package com.vehicle.demo.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceResponseDTO {

    private Long id;
    private Long vehicleId;
    private String policyNumber;
    private String provider;
    private LocalDate startDate;
    private LocalDate expiryDate;
    private Double premiumAmount;
    private Double idv;
    private String coverageType;

    private String documentPath;
    private String documentName;
    private String documentType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String name;
    private String mobileNumber;
}