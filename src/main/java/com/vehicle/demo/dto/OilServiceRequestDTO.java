package com.vehicle.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class OilServiceRequestDTO {

    private Long vehicleId;

    private Integer lastServiceKm;
    private Integer serviceIntervalKm;
    private LocalDate lastServiceDate;

    private String oilBrand;
    private BigDecimal oilQuantityLitres;
    private String serviceVendor;
}
