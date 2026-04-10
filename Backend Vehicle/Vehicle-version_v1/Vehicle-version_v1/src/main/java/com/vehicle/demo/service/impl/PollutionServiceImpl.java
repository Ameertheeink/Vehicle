package com.vehicle.demo.service.impl;

import com.vehicle.demo.dto.InsuranceResponseDTO;
import com.vehicle.demo.dto.PollutionReminderDTO;
import com.vehicle.demo.dto.PollutionRequestDTO;
import com.vehicle.demo.dto.PollutionResponseDTO;
import com.vehicle.demo.entity.Insurance;
import com.vehicle.demo.entity.PollutionCertificate;
import com.vehicle.demo.entity.Vehicle;
import com.vehicle.demo.enums.ReminderStatus;
import com.vehicle.demo.exception.ResourceNotFoundException;
import com.vehicle.demo.repository.PollutionRepository;
import com.vehicle.demo.repository.VehicleRepository;
import com.vehicle.demo.service.PollutionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service
public class PollutionServiceImpl implements PollutionService {

    private final PollutionRepository pollutionRepo;
    private final VehicleRepository vehicleRepo;

    public PollutionServiceImpl(PollutionRepository pollutionRepo,
                                VehicleRepository vehicleRepo) {
        this.pollutionRepo = pollutionRepo;
        this.vehicleRepo = vehicleRepo;
    }

    // =============================
    // CREATE
    // =============================
    @Override
    public PollutionResponseDTO create(PollutionRequestDTO dto) {

        Vehicle vehicle = vehicleRepo.findById(dto.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        PollutionCertificate p = new PollutionCertificate();
        p.setVehicle(vehicle);
        p.setCertificateNumber(dto.getCertificateNumber());
        p.setIssueDate(dto.getIssueDate());
        p.setExpiryDate(dto.getExpiryDate());
        p.setCost(dto.getCost());
        p.setVendor(dto.getVendor());

        PollutionCertificate saved = pollutionRepo.save(p);

        return map(saved);
    }

    // =============================
    // GET ALL
    // =============================
    @Override
    public List<PollutionResponseDTO> getAll() {
        return pollutionRepo.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    // =============================
    // GET BY VEHICLE
    // =============================
    @Override
    public List<PollutionResponseDTO> getByVehicleId(Long vehicleId) {

        return pollutionRepo.findByVehicleId(vehicleId)
                .stream()
                .map(this::map)
                .toList();
    }

    // =============================
    // UPDATE
    // =============================
    @Override
    public PollutionResponseDTO update(Long id, PollutionRequestDTO dto) {

        PollutionCertificate p = pollutionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pollution not found"));

        p.setCertificateNumber(dto.getCertificateNumber());
        p.setIssueDate(dto.getIssueDate());
        p.setExpiryDate(dto.getExpiryDate());
        p.setCost(dto.getCost());
        p.setVendor(dto.getVendor());

        PollutionCertificate updated = pollutionRepo.save(p);

        return map(updated);
    }

    // =============================
    // DELETE
    // =============================
    @Override
    public void delete(Long id) {

        PollutionCertificate p = pollutionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pollution not found"));

        pollutionRepo.delete(p);
    }

    // =============================
    // UPLOAD DOCUMENT (REPLACE OLD)
    // =============================
    @Override
    public void uploadDocument(Long id, MultipartFile file) {

        PollutionCertificate p = pollutionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pollution not found"));

        try {
            String baseDir = System.getProperty("user.dir");

            // 🔥 delete old file
            if (p.getDocumentPath() != null) {
                Path oldPath = Paths.get(baseDir + "/" + p.getDocumentPath());
                Files.deleteIfExists(oldPath);
            }

            String uploadDir = baseDir + "/uploads/pollution/";
            Files.createDirectories(Paths.get(uploadDir));

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path filePath = Paths.get(uploadDir + fileName);
            Files.write(filePath, file.getBytes());

            // ✅ update DB
            p.setDocumentPath("uploads/pollution/" + fileName);
            p.setDocumentName(file.getOriginalFilename());
            p.setDocumentType(file.getContentType());
            p.setUpdatedAt(LocalDateTime.now());

            pollutionRepo.save(p);

        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }

    // =============================
    // DOWNLOAD
    // =============================
    @Override
    public byte[] downloadDocument(Long id) {

        PollutionCertificate p = pollutionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pollution not found"));

        try {
            Path path = Paths.get(System.getProperty("user.dir") + "/" + p.getDocumentPath());
            return Files.readAllBytes(path);

        } catch (IOException e) {
            throw new RuntimeException("Download failed", e);
        }
    }

    @Override
    public String getDocumentType(Long id) {
        return pollutionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pollution not found"))
                .getDocumentType();
    }

    @Override
    public String getDocumentName(Long id) {
        return pollutionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pollution not found"))
                .getDocumentName();
    }

    // =============================
    // REMINDERS
    // =============================
    @Override
    public List<PollutionReminderDTO> getReminders(int thresholdDays) {

        List<Vehicle> vehicles = vehicleRepo.findAll();

        return vehicles.stream()
                .map(vehicle -> {

                    PollutionCertificate p = pollutionRepo
                            .findTopByVehicleIdOrderByExpiryDateDesc(vehicle.getId());

                    if (p == null) return null;

                    long remainingDays = ChronoUnit.DAYS.between(
                            LocalDate.now(),
                            p.getExpiryDate()
                    );

                    ReminderStatus status =
                            calculateStatus(remainingDays, thresholdDays);

                    PollutionReminderDTO dto = new PollutionReminderDTO();
                    dto.setVehicleId(vehicle.getId());
                    dto.setVehicleNumber(vehicle.getVehicleNumber());
                    dto.setRemainingDays(remainingDays);
                    dto.setExpiryDate(p.getExpiryDate());
                    dto.setStatus(status);

                    return dto;
                })
                .filter(Objects::nonNull)
                .sorted((a, b) -> {

                    int p1 = getPriority(a.getStatus());
                    int p2 = getPriority(b.getStatus());

                    if (p1 != p2) return Integer.compare(p1, p2);

                    return Long.compare(a.getRemainingDays(), b.getRemainingDays());
                })
                .toList();
    }

    @Override
    public PollutionReminderDTO getTopReminder(int thresholdDays) {

        return getReminders(thresholdDays)
                .stream()
                .findFirst()
                .orElse(null);
    }

    // =============================
    // HELPER METHODS
    // =============================
    private ReminderStatus calculateStatus(long remainingDays, int threshold) {

        if (remainingDays <= 0) return ReminderStatus.OVERDUE;
        else if (remainingDays <= threshold) return ReminderStatus.DUE_SOON;
        else return ReminderStatus.SAFE;
    }

    private int getPriority(ReminderStatus status) {
        return switch (status) {
            case OVERDUE -> 1;
            case DUE_SOON -> 2;
            case SAFE -> 3;
        };
    }

    private PollutionResponseDTO map(PollutionCertificate p) {

        PollutionResponseDTO dto = new PollutionResponseDTO();

        dto.setId(p.getId());
        dto.setVehicleId(p.getVehicle().getId());
        dto.setCertificateNumber(p.getCertificateNumber());
        dto.setIssueDate(p.getIssueDate());
        dto.setExpiryDate(p.getExpiryDate());
        dto.setCost(p.getCost());
        dto.setVendor(p.getVendor());
        dto.setDocumentName(p.getDocumentName());
        dto.setDocumentType(p.getDocumentType());
        dto.setCreatedAt(p.getCreatedAt());

        return dto;
    }

    @Override
    public PollutionResponseDTO getLatestByVehicleId(Long vehicleId) {

        PollutionCertificate pollution = pollutionRepo
                .findTopByVehicleIdOrderByExpiryDateDesc(vehicleId);

        if (pollution == null) {
            throw new ResourceNotFoundException(
                    "No pollution certificate found for vehicle id: " + vehicleId
            );
        }

        return map(pollution);
    }

    public PollutionResponseDTO updateLatestByVehicleId(Long vehicleId,
                                                        PollutionRequestDTO dto) {

        PollutionCertificate p = pollutionRepo
                .findTopByVehicleIdOrderByExpiryDateDesc(vehicleId);

        if (p == null) {
            throw new ResourceNotFoundException("No pollution record found");
        }

        p.setCertificateNumber(dto.getCertificateNumber());
        p.setIssueDate(dto.getIssueDate());
        p.setExpiryDate(dto.getExpiryDate());
        p.setCost(dto.getCost());
        p.setVendor(dto.getVendor());

        return map(pollutionRepo.save(p));
    }
}