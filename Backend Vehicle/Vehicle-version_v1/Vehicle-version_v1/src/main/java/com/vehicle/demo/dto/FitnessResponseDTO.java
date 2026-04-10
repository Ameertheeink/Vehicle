package com.vehicle.demo.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FitnessResponseDTO {

    private Long id;
    private Long vehicleId;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String documentUrl;
}