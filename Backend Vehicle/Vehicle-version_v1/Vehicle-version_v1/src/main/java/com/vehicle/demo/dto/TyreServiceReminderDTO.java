package com.vehicle.demo.dto;

import com.vehicle.demo.enums.ReminderStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class TyreServiceReminderDTO {

    private Long vehicleId;
    private String vehicleNumber;
    private Integer remainingKm;
    private ReminderStatus status;
}