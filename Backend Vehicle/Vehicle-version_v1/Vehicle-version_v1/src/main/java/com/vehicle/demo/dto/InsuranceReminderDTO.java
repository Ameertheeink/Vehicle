package com.vehicle.demo.dto;

import com.vehicle.demo.enums.ReminderStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class InsuranceReminderDTO {

    private Long vehicleId;
    private String vehicleNumber;
    private long remainingDays;
    private ReminderStatus status;
    private LocalDate expiryDate;
}