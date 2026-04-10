package com.vehicle.demo.service;

import com.vehicle.demo.dto.TyreServiceReminderDTO;
import com.vehicle.demo.dto.TyreServiceRequestDTO;
import com.vehicle.demo.dto.TyreServiceResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TyreServiceService {

    TyreServiceResponseDTO create(TyreServiceRequestDTO dto);

    List<TyreServiceResponseDTO> getAll();

    List<TyreServiceResponseDTO> getByVehicleId(Long vehicleId);

    TyreServiceResponseDTO getById(Long id);

    TyreServiceResponseDTO update(Long id, TyreServiceRequestDTO dto);

    void delete(Long id);

    void uploadBill(Long tyreServiceId, MultipartFile file);

    byte[] downloadBill(Long tyreServiceId);

    String getBillType(Long tyreServiceId);

    String getBillName(Long tyreServiceId);

    List<TyreServiceReminderDTO> getReminders(int threshold);
    TyreServiceReminderDTO getTopReminder(int threshold);

    public TyreServiceResponseDTO getLatestTyreService(Long vehicleId);
    public Page<TyreServiceResponseDTO> getAllPaginated(int page, int size);
    public Page<TyreServiceResponseDTO> getByVehiclePaginated(Long vehicleId, int page, int size);
}