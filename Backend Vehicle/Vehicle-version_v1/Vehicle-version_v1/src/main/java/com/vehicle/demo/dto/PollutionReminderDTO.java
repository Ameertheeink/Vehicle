package com.vehicle.demo.dto;

import com.vehicle.demo.enums.ReminderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PollutionReminderDTO {

    private Long vehicleId;
    private String vehicleNumber;

    private long remainingDays;
    private LocalDate expiryDate;

    private ReminderStatus status;
}