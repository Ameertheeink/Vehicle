package com.vehicle.demo.dto;



import com.vehicle.demo.enums.CertificateStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class FitnessReminderDTO {

    private Long certificateId;
    private Long vehicleId;
    private String vehicleNumber;

    private LocalDate expiryDate;
    private Long remainingDays;

    private CertificateStatus status;
}