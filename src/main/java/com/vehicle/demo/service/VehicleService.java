package com.vehicle.demo.service;

import com.vehicle.demo.dto.UpdateKmRequestDTO;
import com.vehicle.demo.dto.VehicleRequestDTO;
import com.vehicle.demo.dto.VehicleResponseDTO;

import com.vehicle.demo.dto.VehicleUpdateRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service

public interface VehicleService {

    VehicleResponseDTO createVehicle(VehicleRequestDTO requestDTO);
    List<VehicleResponseDTO> getAllVehicles();

    VehicleResponseDTO updateVehicleKm(Long id, UpdateKmRequestDTO requestDTO);


    VehicleResponseDTO updateVehicleKm(String vehicleNumber,
                                       UpdateKmRequestDTO requestDTO);

    void uploadDocument(String vehicleNumber, MultipartFile file);
    byte[] getVehicleDocument(String vehicleNumber);
    VehicleResponseDTO updateVehicle(Long id, VehicleUpdateRequestDTO requestDTO);
    void deleteVehicle(Long id);

   void uploadVehicleImages(Long id, MultipartFile[] files);
   VehicleResponseDTO getVehicleById(Long id);
   List<String> getVehicleImages(Long vehicleId);
   void deleteImagesByVehicleId(Long vehicleId);






}