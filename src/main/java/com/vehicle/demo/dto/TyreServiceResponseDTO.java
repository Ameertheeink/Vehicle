package com.vehicle.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TyreServiceResponseDTO {

    private Long id;
    private Long vehicleId;

    private Integer changeKm;
    private Integer serviceIntervalKm;
    private Integer nextChangeKm;

    private LocalDate changeDate;

    private String tyreBrand;
    private String tyreType;

    private Double cost;
    private String vendor;

    private String billPath;
    private String billName;
    private String billType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // getters & setters
}