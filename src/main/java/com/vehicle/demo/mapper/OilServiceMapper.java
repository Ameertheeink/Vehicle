package com.vehicle.demo.mapper;

import com.vehicle.demo.dto.OilServiceRequestDTO;
import com.vehicle.demo.dto.OilServiceResponseDTO;
import com.vehicle.demo.entity.OilService;
import com.vehicle.demo.entity.Vehicle;

public class OilServiceMapper {

    private OilServiceMapper() {
    }

    // Entity → ResponseDTO
    public static OilServiceResponseDTO toDTO(OilService oilService) {

        return OilServiceResponseDTO.builder()
                .id(oilService.getId())
                .vehicleId(oilService.getVehicle().getId())
                .lastServiceKm(oilService.getLastServiceKm())
                .serviceIntervalKm(oilService.getServiceIntervalKm())
                .lastServiceDate(oilService.getLastServiceDate())
                .oilBrand(oilService.getOilBrand())
                .oilQuantityLitres(oilService.getOilQuantityLitres())
                .serviceVendor(oilService.getServiceVendor())
                .nextDueKm(oilService.getNextDueKm())
                .createdAt(oilService.getCreatedAt())
                .updatedAt(oilService.getUpdatedAt())
                .build();
    }

    // RequestDTO → Entity
    public static OilService toEntity(OilServiceRequestDTO dto, Vehicle vehicle) {

        OilService oilService = new OilService();

        oilService.setVehicle(vehicle);
        oilService.setLastServiceKm(dto.getLastServiceKm());
        oilService.setServiceIntervalKm(dto.getServiceIntervalKm());
        oilService.setLastServiceDate(dto.getLastServiceDate());
        oilService.setOilBrand(dto.getOilBrand());
        oilService.setOilQuantityLitres(dto.getOilQuantityLitres());
        oilService.setServiceVendor(dto.getServiceVendor());

        return oilService;
    }
}
