package com.vehicle.demo.service.impl;

import com.vehicle.demo.dto.FitnessReminderDTO;
import com.vehicle.demo.dto.FitnessRequestDTO;
import com.vehicle.demo.dto.FitnessResponseDTO;
import com.vehicle.demo.entity.FitnessCertificate;
import com.vehicle.demo.enums.CertificateStatus;
import com.vehicle.demo.repository.FitnessCertificateRepository;
import com.vehicle.demo.repository.VehicleRepository;
import com.vehicle.demo.service.FitnessService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FitnessServiceImpl implements FitnessService {

    private final FitnessCertificateRepository fitnessRepository;
    private final VehicleRepository vehicleRepository;

    // 👉 change folder as per your project
    private final String UPLOAD_DIR = "uploads/fitness/";

    // ✅ CREATE
    @Override
    public FitnessResponseDTO create(FitnessRequestDTO dto) {

        vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        if (fitnessRepository.findByVehicleId(dto.getVehicleId()).isPresent()) {
            throw new RuntimeException("Fitness already exists for this vehicle");
        }

        FitnessCertificate cert = new FitnessCertificate();
        cert.setVehicleId(dto.getVehicleId());
        cert.setIssueDate(dto.getIssueDate());
        cert.setExpiryDate(dto.getExpiryDate());
        cert.setCreatedAt(LocalDateTime.now());

        return mapToDTO(fitnessRepository.save(cert));
    }

    // ✅ GET
    @Override
    public FitnessResponseDTO getByVehicleId(Long vehicleId) {

        FitnessCertificate cert = fitnessRepository.findByVehicleId(vehicleId)
                .orElseThrow(() -> new RuntimeException("Fitness not found"));

        return mapToDTO(cert);
    }

    // ✅ UPDATE
    @Override
    public FitnessResponseDTO update(Long id, FitnessRequestDTO dto) {

        FitnessCertificate cert = fitnessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fitness not found"));

        cert.setIssueDate(dto.getIssueDate());
        cert.setExpiryDate(dto.getExpiryDate());
        cert.setUpdatedAt(LocalDateTime.now());

        return mapToDTO(fitnessRepository.save(cert));
    }

    // ✅ DELETE (WITH FILE DELETE)
    @Override
    public void delete(Long id) {

        FitnessCertificate cert = fitnessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fitness not found"));

        // delete file
        if (cert.getDocumentPath() != null) {
            new File(cert.getDocumentPath()).delete();
        }

        fitnessRepository.delete(cert);
    }

    // ✅ UPLOAD DOCUMENT (REPLACE OLD)
    @Override
    public void uploadDocument(Long id, MultipartFile file) {

        FitnessCertificate cert = fitnessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fitness not found"));

        try {
            // create folder if not exists
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            // delete old file
            if (cert.getDocumentPath() != null) {
                new File(cert.getDocumentPath()).delete();
            }

            // generate unique filename
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            String filePath = UPLOAD_DIR + fileName;

            // save file
            Files.write(Paths.get(filePath), file.getBytes());

            cert.setDocumentPath(filePath);
            cert.setDocumentType(file.getContentType());

            fitnessRepository.save(cert);

        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }

    // ✅ DOWNLOAD DOCUMENT
    @Override
    public byte[] downloadDocument(Long id) {

        FitnessCertificate cert = fitnessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fitness not found"));

        try {
            return Files.readAllBytes(Paths.get(cert.getDocumentPath()));
        } catch (IOException e) {
            throw new RuntimeException("File not found");
        }
    }

    // ✅ GET DOCUMENT TYPE
    @Override
    public String getDocumentType(Long id) {

        return fitnessRepository.findById(id)
                .map(FitnessCertificate::getDocumentType)
                .orElse(null);
    }

    // ✅ REMINDER
    @Override
    public List<FitnessReminderDTO> getReminders(int days) {

        LocalDate today = LocalDate.now();

        return fitnessRepository.findAll().stream()
                .map(cert -> {

                    long remaining = ChronoUnit.DAYS.between(today, cert.getExpiryDate());

                    CertificateStatus status;
                    if (remaining < 0) status = CertificateStatus.EXPIRED;
                    else if (remaining <= days) status = CertificateStatus.EXPIRING_SOON;
                    else status = CertificateStatus.VALID;

                    // 👉 fetch vehicle number
                    String vehicleNumber = vehicleRepository.findById(cert.getVehicleId())
                            .map(v -> v.getVehicleNumber())
                            .orElse("N/A");

                    return FitnessReminderDTO.builder()
                            .certificateId(cert.getId())
                            .vehicleId(cert.getVehicleId())
                            .vehicleNumber(vehicleNumber)
                            .expiryDate(cert.getExpiryDate())
                            .remainingDays(remaining)
                            .status(status)
                            .build();
                })
                .sorted(Comparator.comparing(FitnessReminderDTO::getRemainingDays))
                .toList();
    }
    // ✅ DTO MAPPER
    private FitnessResponseDTO mapToDTO(FitnessCertificate cert) {

        FitnessResponseDTO dto = new FitnessResponseDTO();
        dto.setId(cert.getId());
        dto.setVehicleId(cert.getVehicleId());
        dto.setIssueDate(cert.getIssueDate());
        dto.setExpiryDate(cert.getExpiryDate());

        if (cert.getDocumentPath() != null) {
            dto.setDocumentUrl("/fitness/" + cert.getId() + "/document");
        }

        return dto;
    }
}