package com.vehicle.demo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Setter
@Getter
public class VehicleRegistrationRequestDTO {

    private Long vehicleId;
    private String registrationNumber;
    private LocalDate registrationDate;
    private String chassisNumber;
    private String engineNumber;
    private String registrationAuthority;
    private String makersName;
    private String modelName;
    private String vehicleClass;
    private String bodyType;
    private String colour;
    private String fuelType;
    private String emissionNorms;
    private String manufacturingMonthYear;
    private Integer cubicCapacity;
    private Integer seatingCapacity;
    private String ownerName;
    private String fatherOrHusbandName;
    private String ownerSerialNumber;
    private String address;
    private String pinCode;
}