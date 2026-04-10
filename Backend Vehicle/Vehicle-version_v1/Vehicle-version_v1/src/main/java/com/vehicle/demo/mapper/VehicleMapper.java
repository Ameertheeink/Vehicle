package com.vehicle.demo.mapper;



import com.vehicle.demo.dto.VehicleRequestDTO;
import com.vehicle.demo.dto.VehicleResponseDTO;
import com.vehicle.demo.entity.Vehicle;
import org.springframework.stereotype.Component;


@Component
public class VehicleMapper {

    public Vehicle toEntity(VehicleRequestDTO dto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber(dto.getVehicleNumber());
        vehicle.setOwnerName(dto.getOwnerName());
        vehicle.setVehicleType(dto.getVehicleType());
        vehicle.setBrand(dto.getBrand());
        vehicle.setModel(dto.getModel());
        vehicle.setFuelType(dto.getFuelType());
        vehicle.setManufacturingYear(dto.getManufacturingYear());
        vehicle.setColor(dto.getColor());
        vehicle.setMobileNumber(dto.getMobileNumber());
        vehicle.setCurrentKm(dto.getCurrentKm() != null ? dto.getCurrentKm() : 0);
        return vehicle;
    }

    public VehicleResponseDTO toResponseDTO(Vehicle vehicle) {
        VehicleResponseDTO dto = new VehicleResponseDTO();
        dto.setId(vehicle.getId());
        dto.setVehicleNumber(vehicle.getVehicleNumber());
        dto.setOwnerName(vehicle.getOwnerName());
        dto.setVehicleType(vehicle.getVehicleType());
        dto.setBrand(vehicle.getBrand());
        dto.setModel(vehicle.getModel());
        dto.setFuelType(vehicle.getFuelType());
        dto.setManufacturingYear(vehicle.getManufacturingYear());
        dto.setColor(vehicle.getColor());
        dto.setCurrentKm(vehicle.getCurrentKm());
        dto.setCreatedAt(vehicle.getCreatedAt());
        dto.setMobileNumber(vehicle.getMobileNumber());
        return dto;
    }
}
