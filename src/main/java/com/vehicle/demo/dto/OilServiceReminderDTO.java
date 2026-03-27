package com.vehicle.demo.dto;

import com.vehicle.demo.enums.ReminderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OilServiceReminderDTO {

    private Long vehicleId;
    private String vehicleNumber;
    private Integer remainingKm;
    private ReminderStatus status;
}