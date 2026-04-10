package com.vehicle.demo.service;

import com.vehicle.demo.dto.InsuranceReminderDTO;
import com.vehicle.demo.dto.InsuranceRequestDTO;
import com.vehicle.demo.dto.InsuranceResponseDTO;
import com.vehicle.demo.entity.Insurance;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InsuranceService {

    InsuranceResponseDTO create(InsuranceRequestDTO dto);

    List<InsuranceResponseDTO> getAll();

    List<InsuranceResponseDTO> getByVehicleId(Long vehicleId);

    InsuranceResponseDTO update(Long id, InsuranceRequestDTO dto);

    void delete(Long id);

    void uploadDocument(Long id, MultipartFile file);

    byte[] downloadDocument(Long id);

    String getDocumentType(Long id);

    String getDocumentName(Long id);

    List<InsuranceReminderDTO> getReminders(int thresholdDays);

    InsuranceReminderDTO getTopReminder(int thresholdDays);
    InsuranceResponseDTO getLatestByVehicleId(Long vehicleId);

}