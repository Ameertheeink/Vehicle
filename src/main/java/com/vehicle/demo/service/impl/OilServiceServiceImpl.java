package com.vehicle.demo.service.impl;

import com.vehicle.demo.dto.OilServiceRequestDTO;
import com.vehicle.demo.dto.OilServiceResponseDTO;
import com.vehicle.demo.entity.OilService;
import com.vehicle.demo.entity.Vehicle;
import com.vehicle.demo.exception.ResourceNotFoundException;
import com.vehicle.demo.repository.OilServiceRepository;
import com.vehicle.demo.repository.VehicleRepository;
import com.vehicle.demo.service.OilServiceService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.vehicle.demo.mapper.OilServiceMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OilServiceServiceImpl implements OilServiceService {

    private final OilServiceRepository oilServiceRepository;
    private final VehicleRepository vehicleRepository;

    public OilServiceServiceImpl(OilServiceRepository oilServiceRepository,
                                 VehicleRepository vehicleRepository) {
        this.oilServiceRepository = oilServiceRepository;
        this.vehicleRepository = vehicleRepository;
    }

    // =============================
    // CREATE OIL SERVICE
    // =============================
    @Override
    public OilServiceResponseDTO createOilService(OilServiceRequestDTO requestDTO) {

        Vehicle vehicle = vehicleRepository.findById(requestDTO.getVehicleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle not found with id: " + requestDTO.getVehicleId()));

        OilService oilService = new OilService();
        oilService.setVehicle(vehicle);
        oilService.setLastServiceKm(requestDTO.getLastServiceKm());
        oilService.setServiceIntervalKm(requestDTO.getServiceIntervalKm());
        oilService.setLastServiceDate(requestDTO.getLastServiceDate());
        oilService.setOilBrand(requestDTO.getOilBrand());
        oilService.setOilQuantityLitres(requestDTO.getOilQuantityLitres());
        oilService.setServiceVendor(requestDTO.getServiceVendor());

        OilService saved = oilServiceRepository.save(oilService);

        return mapToResponseDTO(saved);
    }

    // =============================
    // GET ALL BY VEHICLE
    // =============================
    @Override
    public List<OilServiceResponseDTO> getOilServicesByVehicleId(Long vehicleId) {

        return oilServiceRepository.findByVehicleId(vehicleId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // =============================
    // GET BY ID
    // =============================
    @Override
    public OilServiceResponseDTO getOilServiceById(Long id) {

        OilService oilService = oilServiceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Oil Service not found with id: " + id));

        return mapToResponseDTO(oilService);
    }

    // =============================
    // DELETE
    // =============================
    @Override
    public void deleteOilService(Long id) {

        OilService oilService = oilServiceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Oil Service not found with id: " + id));

        oilServiceRepository.delete(oilService);
    }

    // =============================
    // UPLOAD SERVICE BILL
    // =============================
    @Override
    public void uploadServiceBill(Long oilServiceId, MultipartFile file) {

        OilService oilService = oilServiceRepository.findById(oilServiceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Oil Service not found with id: " + oilServiceId));

        try {

            String uploadDir = System.getProperty("user.dir") + "/uploads/oil/";
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get(uploadDir));

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            java.nio.file.Path filePath =
                    java.nio.file.Paths.get(uploadDir + fileName);

            java.nio.file.Files.write(filePath, file.getBytes());

            oilService.setServiceBillPath("uploads/oil/" + fileName);
            oilService.setServiceBillName(file.getOriginalFilename());
            oilService.setServiceBillType(file.getContentType());
            oilService.setUpdatedAt(LocalDateTime.now());

            oilServiceRepository.save(oilService);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload service bill", e);
        }
    }

    // =============================
    // MAPPER
    // =============================
    private OilServiceResponseDTO mapToResponseDTO(OilService oilService) {

        OilServiceResponseDTO dto = new OilServiceResponseDTO();

        dto.setId(oilService.getId());
        dto.setVehicleId(oilService.getVehicle().getId());
        dto.setLastServiceKm(oilService.getLastServiceKm());
        dto.setServiceIntervalKm(oilService.getServiceIntervalKm());
        dto.setLastServiceDate(oilService.getLastServiceDate());
        dto.setOilBrand(oilService.getOilBrand());
        dto.setOilQuantityLitres(oilService.getOilQuantityLitres());
        dto.setServiceVendor(oilService.getServiceVendor());
        dto.setNextDueKm(oilService.getNextDueKm());

        dto.setServiceBillPath(oilService.getServiceBillPath());
        dto.setServiceBillName(oilService.getServiceBillName());
        dto.setServiceBillType(oilService.getServiceBillType());

        dto.setCreatedAt(oilService.getCreatedAt());
        dto.setUpdatedAt(oilService.getUpdatedAt());

        return dto;
    }
    @Override
    public byte[] downloadServiceBill(Long oilServiceId) {

        OilService oilService = oilServiceRepository.findById(oilServiceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Oil Service not found with id: " + oilServiceId));

        try {
            java.nio.file.Path path =
                    java.nio.file.Paths.get(System.getProperty("user.dir") + "/" + oilService.getServiceBillPath());

            return java.nio.file.Files.readAllBytes(path);

        } catch (IOException e) {
            throw new RuntimeException("Failed to download service bill", e);
        }
    }

    @Override
    public String getServiceBillType(Long oilServiceId) {

        OilService oilService = oilServiceRepository.findById(oilServiceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Oil Service not found"));

        return oilService.getServiceBillType();
    }

    @Override
    public String getServiceBillName(Long oilServiceId) {

        OilService oilService = oilServiceRepository.findById(oilServiceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Oil Service not found"));

        return oilService.getServiceBillName();
    }
    @Override
    public List<OilServiceResponseDTO> getAllOilServices() {

        return oilServiceRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public OilServiceResponseDTO updateOilService(Long id,
                                                  OilServiceRequestDTO requestDTO) {

        OilService oilService = oilServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oil service not found"));

        oilService.setLastServiceKm(requestDTO.getLastServiceKm());
        oilService.setServiceIntervalKm(requestDTO.getServiceIntervalKm());
        oilService.setLastServiceDate(requestDTO.getLastServiceDate());
        oilService.setOilBrand(requestDTO.getOilBrand());
        oilService.setOilQuantityLitres(requestDTO.getOilQuantityLitres());
        oilService.setServiceVendor(requestDTO.getServiceVendor());

        // recalculate nextDueKm
        oilService.setNextDueKm(
                requestDTO.getLastServiceKm() + requestDTO.getServiceIntervalKm()
        );

        OilService updated = oilServiceRepository.save(oilService);

        return mapToResponseDTO(updated);
    }


    @Override
    public void deleteBill(Long id) {

        OilService oilService = oilServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oil service not found"));

        if (oilService.getServiceBillPath() == null) {
            throw new RuntimeException("Bill not found");
        }

        try {
            Files.deleteIfExists(Paths.get(oilService.getServiceBillPath()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete bill file");
        }

        oilService.setServiceBillPath(null);
        oilService.setServiceBillName(null);
        oilService.setServiceBillType(null);

        oilServiceRepository.save(oilService);
    }



}
