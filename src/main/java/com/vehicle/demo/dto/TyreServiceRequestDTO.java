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
public class TyreServiceRequestDTO {

    private Long vehicleId;
    private Integer changeKm;
    private Integer serviceIntervalKm;
    private LocalDate changeDate;

    private String tyreBrand;
    private String tyreType;

    private Double cost;
    private String vendor;

    // getters & setters
}