package com.vehicle.demo.service.impl;

import com.vehicle.demo.dto.OilServiceReminderDTO;
import com.vehicle.demo.dto.OilServiceRequestDTO;
import com.vehicle.demo.dto.OilServiceResponseDTO;
import com.vehicle.demo.entity.OilService;
import com.vehicle.demo.entity.Vehicle;
import com.vehicle.demo.enums.ReminderStatus;
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
            String baseDir = System.getProperty("user.dir");

            // 🔥 STEP 1: VALIDATE FILE TYPE FIRST
            String contentType = file.getContentType();

            if (contentType == null ||
                    !(contentType.equals("application/pdf") ||
                            contentType.equals("image/jpeg") ||
                            contentType.equals("image/png"))) {

                throw new RuntimeException("Only PDF, JPEG, PNG files are allowed");
            }

            String originalFileName = file.getOriginalFilename();

            if (originalFileName == null ||
                    !(originalFileName.toLowerCase().endsWith(".pdf") ||
                            originalFileName.toLowerCase().endsWith(".jpg") ||
                            originalFileName.toLowerCase().endsWith(".jpeg") ||
                            originalFileName.toLowerCase().endsWith(".png"))) {

                throw new RuntimeException("Invalid file type");
            }

            // 🔥 STEP 2: DELETE OLD FILE IF EXISTS
            if (oilService.getServiceBillPath() != null) {

                java.nio.file.Path oldFilePath = java.nio.file.Paths.get(
                        baseDir + "/" + oilService.getServiceBillPath()
                );

                java.nio.file.Files.deleteIfExists(oldFilePath);
            }

            // 🔥 STEP 3: CREATE DIRECTORY
            String uploadDir = baseDir + "/uploads/oil/";
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get(uploadDir));

            // 🔥 STEP 4: SAVE NEW FILE
            String newFileName = System.currentTimeMillis() + "_" + originalFileName;

            java.nio.file.Path filePath =
                    java.nio.file.Paths.get(uploadDir + newFileName);

            java.nio.file.Files.write(filePath, file.getBytes());

            // 🔥 STEP 5: UPDATE DB
            oilService.setServiceBillPath("uploads/oil/" + newFileName);
            oilService.setServiceBillName(originalFileName);
            oilService.setServiceBillType(contentType);
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
            Files.deleteIfExists(
                    Paths.get(System.getProperty("user.dir") + "/" + oilService.getServiceBillPath())
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete bill file");
        }

        oilService.setServiceBillPath(null);
        oilService.setServiceBillName(null);
        oilService.setServiceBillType(null);

        oilServiceRepository.save(oilService);
    }



    @Override
    public List<OilServiceReminderDTO> getReminders(int threshold) {

        List<Vehicle> vehicles = vehicleRepository.findAll();

        return vehicles.stream()
                .map(vehicle -> {

                    OilService service = oilServiceRepository
                            .findTopByVehicleIdOrderByLastServiceDateDesc(vehicle.getId());

                    // Skip vehicles with no oil service
                    if (service == null) {
                        return null;
                    }

                    int remainingKm = service.getNextDueKm() - vehicle.getCurrentKm();

                    ReminderStatus status = calculateStatus(remainingKm, threshold);

                    OilServiceReminderDTO dto = new OilServiceReminderDTO();
                    dto.setVehicleId(vehicle.getId());
                    dto.setVehicleNumber(vehicle.getVehicleNumber());
                    dto.setRemainingKm(remainingKm);
                    dto.setStatus(status);

                    return dto;
                })
                .filter(dto -> dto != null)
                .sorted((a, b) -> {

                    int priorityA = getPriority(a.getStatus());
                    int priorityB = getPriority(b.getStatus());

                    if (priorityA != priorityB) {
                        return Integer.compare(priorityA, priorityB);
                    }

                    return Integer.compare(a.getRemainingKm(), b.getRemainingKm());
                })
                .toList();
    }
    private ReminderStatus calculateStatus(int remainingKm, int threshold) {
        if (remainingKm <= 0) {
            return ReminderStatus.OVERDUE;
        } else if (remainingKm <= threshold) {
            return ReminderStatus.DUE_SOON;
        } else {
            return ReminderStatus.SAFE;
        }
    }

    private int getPriority(ReminderStatus status) {
        switch (status) {
            case OVERDUE: return 1;
            case DUE_SOON: return 2;
            case SAFE: return 3;
            default: return 4;
        }
    }

}
