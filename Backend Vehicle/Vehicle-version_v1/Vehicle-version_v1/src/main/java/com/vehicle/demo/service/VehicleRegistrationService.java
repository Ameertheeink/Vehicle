package com.vehicle.demo.service;

import com.vehicle.demo.dto.VehicleRegistrationRequestDTO;
import com.vehicle.demo.dto.VehicleRegistrationResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VehicleRegistrationService {

    VehicleRegistrationResponseDTO create(VehicleRegistrationRequestDTO dto);

    List<VehicleRegistrationResponseDTO> getByVehicleId(Long vehicleId);

    VehicleRegistrationResponseDTO getLatestByVehicleId(Long vehicleId);

    VehicleRegistrationResponseDTO update(Long id, VehicleRegistrationRequestDTO dto);

    void delete(Long id);

    void uploadDocument(Long id, MultipartFile file);

    byte[] downloadDocument(Long id);

    String getDocumentType(Long id);

    String getDocumentName(Long id);
}