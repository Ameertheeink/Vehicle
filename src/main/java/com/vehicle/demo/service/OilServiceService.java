package com.vehicle.demo.service;

import com.vehicle.demo.dto.OilServiceReminderDTO;
import com.vehicle.demo.dto.OilServiceRequestDTO;
import com.vehicle.demo.dto.OilServiceResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OilServiceService {

    OilServiceResponseDTO createOilService(OilServiceRequestDTO requestDTO);

    List<OilServiceResponseDTO> getOilServicesByVehicleId(Long vehicleId);

    OilServiceResponseDTO getOilServiceById(Long id);

    void deleteOilService(Long id);

    void uploadServiceBill(Long oilServiceId, MultipartFile file);


    public byte[] downloadServiceBill(Long oilServiceId) ;


    public String getServiceBillType(Long oilServiceId) ;


    public String getServiceBillName(Long oilServiceId) ;

    List<OilServiceResponseDTO> getAllOilServices();
    List<OilServiceReminderDTO> getReminders(int threshold);
    OilServiceResponseDTO updateOilService(Long id, OilServiceRequestDTO requestDTO);

    void deleteBill(Long id);





}
