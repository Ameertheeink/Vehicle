package com.vehicle.demo.service;

import com.vehicle.demo.dto.FitnessReminderDTO;
import com.vehicle.demo.dto.FitnessRequestDTO;
import com.vehicle.demo.dto.FitnessResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FitnessService {

    FitnessResponseDTO create(FitnessRequestDTO dto);

    FitnessResponseDTO getByVehicleId(Long vehicleId);

    FitnessResponseDTO update(Long id, FitnessRequestDTO dto);

    void delete(Long id);

    void uploadDocument(Long id, MultipartFile file);

    byte[] downloadDocument(Long id);

    String getDocumentType(Long id);

    List<FitnessReminderDTO> getReminders(int days);
}