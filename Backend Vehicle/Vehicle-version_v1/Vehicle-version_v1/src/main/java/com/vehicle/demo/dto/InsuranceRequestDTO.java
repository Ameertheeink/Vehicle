package com.vehicle.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceRequestDTO {

    private Long vehicleId;
    private String policyNumber;
    private String provider;
    private LocalDate startDate;
    private LocalDate expiryDate;
    private Double premiumAmount;
    private String coverageType;
    private Double idv;
    private String name;
    private String mobileNumber;
}