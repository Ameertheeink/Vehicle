package com.vehicle.demo.service;

import com.vehicle.demo.dto.InsuranceResponseDTO;
import com.vehicle.demo.dto.PollutionReminderDTO;
import com.vehicle.demo.dto.PollutionRequestDTO;
import com.vehicle.demo.dto.PollutionResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PollutionService {

    PollutionResponseDTO create(PollutionRequestDTO dto);

    List<PollutionResponseDTO> getByVehicleId(Long vehicleId);

    List<PollutionResponseDTO> getAll();

    PollutionResponseDTO update(Long id, PollutionRequestDTO dto);

    void delete(Long id);

    void uploadDocument(Long id, MultipartFile file);

    byte[] downloadDocument(Long id);

    String getDocumentType(Long id);

    String getDocumentName(Long id);

    List<PollutionReminderDTO> getReminders(int thresholdDays);

    PollutionReminderDTO getTopReminder(int thresholdDays);

    PollutionResponseDTO getLatestByVehicleId(Long vehicleId);
    public PollutionResponseDTO updateLatestByVehicleId(Long vehicleId,
                                                        PollutionRequestDTO dto);
}