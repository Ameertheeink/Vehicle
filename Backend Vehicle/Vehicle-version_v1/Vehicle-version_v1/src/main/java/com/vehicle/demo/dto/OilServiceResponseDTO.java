package com.vehicle.demo.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OilServiceResponseDTO {

    private Long id;

    private Long vehicleId;

    private Integer lastServiceKm;
    private Integer serviceIntervalKm;
    private LocalDate lastServiceDate;

    private String oilBrand;
    private BigDecimal oilQuantityLitres;
    private String serviceVendor;

    private Integer nextDueKm;

    private String serviceBillPath;
    private String serviceBillName;
    private String serviceBillType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
