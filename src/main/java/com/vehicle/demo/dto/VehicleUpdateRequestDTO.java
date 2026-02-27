package com.vehicle.demo.dto;

import lombok.Data;

@Data
public class VehicleUpdateRequestDTO {

    private String ownerName;
    private String vehicleType;
    private String brand;
    private String model;
    private String fuelType;
    private Integer manufacturingYear;
    private String color;
    private Integer currentKm;
    private String updatedBy;
}