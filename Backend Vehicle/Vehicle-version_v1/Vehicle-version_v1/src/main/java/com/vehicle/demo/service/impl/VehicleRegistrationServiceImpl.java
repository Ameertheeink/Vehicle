package com.vehicle.demo.service.impl;



import com.vehicle.demo.dto.VehicleRegistrationRequestDTO;
import com.vehicle.demo.dto.VehicleRegistrationResponseDTO;
import com.vehicle.demo.entity.Vehicle;
import com.vehicle.demo.entity.VehicleRegistration;
import com.vehicle.demo.exception.ResourceNotFoundException;
import com.vehicle.demo.repository.VehicleRegistrationRepository;
import com.vehicle.demo.repository.VehicleRepository;
import com.vehicle.demo.service.VehicleRegistrationService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VehicleRegistrationServiceImpl implements VehicleRegistrationService {

    private final VehicleRegistrationRepository repo;
    private final VehicleRepository vehicleRepo;

    public VehicleRegistrationServiceImpl(
            VehicleRegistrationRepository repo,
            VehicleRepository vehicleRepo) {
        this.repo = repo;
        this.vehicleRepo = vehicleRepo;
    }

    // =====================================
    // CREATE
    // =====================================
    @Override
    public VehicleRegistrationResponseDTO create(VehicleRegistrationRequestDTO dto) {

        Vehicle vehicle = vehicleRepo.findById(dto.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        VehicleRegistration rc = new VehicleRegistration();

        rc.setVehicle(vehicle);
        mapRequestToEntity(dto, rc);

        VehicleRegistration saved = repo.save(rc);

        return map(saved);
    }

    // =====================================
    // GET BY VEHICLE
    // =====================================
    @Override
    public List<VehicleRegistrationResponseDTO> getByVehicleId(Long vehicleId) {

        return repo.findByVehicleId(vehicleId)
                .stream()
                .map(this::map)
                .toList();
    }

    // =====================================
    // GET LATEST
    // =====================================
    @Override
    public VehicleRegistrationResponseDTO getLatestByVehicleId(Long vehicleId) {

        VehicleRegistration rc =
                repo.findTopByVehicleIdOrderByCreatedAtDesc(vehicleId);

        if (rc == null) {
            throw new ResourceNotFoundException("No RC found for vehicle");
        }

        return map(rc);
    }

    // =====================================
    // UPDATE
    // =====================================
    @Override
    public VehicleRegistrationResponseDTO update(Long id,
                                                 VehicleRegistrationRequestDTO dto) {

        VehicleRegistration rc = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RC not found"));

        mapRequestToEntity(dto, rc);

        VehicleRegistration updated = repo.save(rc);

        return map(updated);
    }

    // =====================================
    // DELETE
    // =====================================
    @Override
    public void delete(Long id) {

        VehicleRegistration rc = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RC not found"));

        repo.delete(rc);
    }

    // =====================================
    // UPLOAD DOCUMENT (REPLACE OLD)
    // =====================================
    @Override
    public void uploadDocument(Long id, MultipartFile file) {

        VehicleRegistration rc = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RC not found"));

        try {
            String baseDir = System.getProperty("user.dir");

            // 🔥 DELETE OLD FILE
            if (rc.getDocumentPath() != null) {
                Path oldPath = Paths.get(baseDir + "/" + rc.getDocumentPath());
                Files.deleteIfExists(oldPath);
            }

            // 🔥 CREATE DIRECTORY
            String uploadDir = baseDir + "/uploads/rc/";
            Files.createDirectories(Paths.get(uploadDir));

            // 🔥 SAVE NEW FILE
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path filePath = Paths.get(uploadDir + fileName);
            Files.write(filePath, file.getBytes());

            // 🔥 UPDATE DB
            rc.setDocumentPath("uploads/rc/" + fileName);
            rc.setDocumentName(file.getOriginalFilename());
            rc.setDocumentType(file.getContentType());
            rc.setUpdatedAt(LocalDateTime.now());

            repo.save(rc);

        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }

    // =====================================
    // DOWNLOAD
    // =====================================
    @Override
    public byte[] downloadDocument(Long id) {

        VehicleRegistration rc = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RC not found"));

        try {
            Path path = Paths.get(
                    System.getProperty("user.dir") + "/" + rc.getDocumentPath()
            );

            return Files.readAllBytes(path);

        } catch (IOException e) {
            throw new RuntimeException("Download failed", e);
        }
    }

    @Override
    public String getDocumentType(Long id) {

        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RC not found"))
                .getDocumentType();
    }

    @Override
    public String getDocumentName(Long id) {

        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RC not found"))
                .getDocumentName();
    }

    // =====================================
    // MAPPER METHODS
    // =====================================
    private VehicleRegistrationResponseDTO map(VehicleRegistration rc) {

        VehicleRegistrationResponseDTO dto = new VehicleRegistrationResponseDTO();

        dto.setId(rc.getId());
        dto.setVehicleId(rc.getVehicle().getId());
        dto.setRegistrationNumber(rc.getRegistrationNumber());
        dto.setRegistrationDate(rc.getRegistrationDate());
        dto.setChassisNumber(rc.getChassisNumber());
        dto.setEngineNumber(rc.getEngineNumber());
        dto.setRegistrationAuthority(rc.getRegistrationAuthority());
        dto.setMakersName(rc.getMakersName());
        dto.setModelName(rc.getModelName());
        dto.setVehicleClass(rc.getVehicleClass());
        dto.setBodyType(rc.getBodyType());
        dto.setColour(rc.getColour());
        dto.setFuelType(rc.getFuelType());
        dto.setEmissionNorms(rc.getEmissionNorms());
        dto.setManufacturingMonthYear(rc.getManufacturingMonthYear());
        dto.setCubicCapacity(rc.getCubicCapacity());
        dto.setSeatingCapacity(rc.getSeatingCapacity());
        dto.setOwnerName(rc.getOwnerName());
        dto.setFatherOrHusbandName(rc.getFatherOrHusbandName());
        dto.setAddress(rc.getAddress());
        dto.setPinCode(rc.getPinCode());
        dto.setDocumentName(rc.getDocumentName());
        dto.setDocumentType(rc.getDocumentType());
        dto.setOwnerSerialNumber(rc.getOwnerSerialNumber());

        return dto;
    }

    private void mapRequestToEntity(VehicleRegistrationRequestDTO dto,
                                    VehicleRegistration rc) {

        rc.setRegistrationNumber(dto.getRegistrationNumber());
        rc.setRegistrationDate(dto.getRegistrationDate());
        rc.setChassisNumber(dto.getChassisNumber());
        rc.setEngineNumber(dto.getEngineNumber());
        rc.setRegistrationAuthority(dto.getRegistrationAuthority());
        rc.setMakersName(dto.getMakersName());
        rc.setModelName(dto.getModelName());
        rc.setVehicleClass(dto.getVehicleClass());
        rc.setBodyType(dto.getBodyType());
        rc.setColour(dto.getColour());
        rc.setFuelType(dto.getFuelType());
        rc.setEmissionNorms(dto.getEmissionNorms());
        rc.setManufacturingMonthYear(dto.getManufacturingMonthYear());
        rc.setCubicCapacity(dto.getCubicCapacity());
        rc.setSeatingCapacity(dto.getSeatingCapacity());
        rc.setOwnerName(dto.getOwnerName());
        rc.setFatherOrHusbandName(dto.getFatherOrHusbandName());
        rc.setAddress(dto.getAddress());
        rc.setPinCode(dto.getPinCode());
        rc.setOwnerSerialNumber(dto.getOwnerSerialNumber());
    }
}