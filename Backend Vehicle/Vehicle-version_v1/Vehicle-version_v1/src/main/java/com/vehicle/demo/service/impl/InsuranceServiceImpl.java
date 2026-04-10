package com.vehicle.demo.service.impl;



import com.vehicle.demo.dto.*;
import com.vehicle.demo.entity.Insurance;
import com.vehicle.demo.entity.Vehicle;
import com.vehicle.demo.enums.ReminderStatus;
import com.vehicle.demo.exception.ResourceNotFoundException;
import com.vehicle.demo.repository.InsuranceRepository;
import com.vehicle.demo.repository.VehicleRepository;
import com.vehicle.demo.service.InsuranceService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class InsuranceServiceImpl implements InsuranceService {

    private final InsuranceRepository insuranceRepo;
    private final VehicleRepository vehicleRepo;

    public InsuranceServiceImpl(InsuranceRepository insuranceRepo,
                                VehicleRepository vehicleRepo) {
        this.insuranceRepo = insuranceRepo;
        this.vehicleRepo = vehicleRepo;
    }

    // =============================
    // CREATE
    // =============================
    @Override
    public InsuranceResponseDTO create(InsuranceRequestDTO dto) {

        Vehicle vehicle = vehicleRepo.findById(dto.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        Insurance i = new Insurance();
        i.setVehicle(vehicle);
        i.setPolicyNumber(dto.getPolicyNumber());
        i.setProvider(dto.getProvider());
        i.setStartDate(dto.getStartDate());
        i.setExpiryDate(dto.getExpiryDate());
        i.setPremiumAmount(dto.getPremiumAmount());
        i.setCoverageType(dto.getCoverageType());
        i.setIdv(dto.getIdv());
        i.setName(dto.getName());
        i.setMobileNumber(dto.getMobileNumber());

        return map(insuranceRepo.save(i));
    }

    // =============================
    // GET ALL
    // =============================
    @Override
    public List<InsuranceResponseDTO> getAll() {
        return insuranceRepo.findAll().stream().map(this::map).toList();
    }

    // =============================
    // GET BY VEHICLE
    // =============================
    @Override
    public List<InsuranceResponseDTO> getByVehicleId(Long vehicleId) {
        return insuranceRepo.findByVehicleId(vehicleId)
                .stream().map(this::map).toList();
    }

    // =============================
    // UPDATE
    // =============================
    @Override
    public InsuranceResponseDTO update(Long id, InsuranceRequestDTO dto) {

        Insurance i = insuranceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance not found"));

        i.setPolicyNumber(dto.getPolicyNumber());
        i.setProvider(dto.getProvider());
        i.setStartDate(dto.getStartDate());
        i.setExpiryDate(dto.getExpiryDate());
        i.setPremiumAmount(dto.getPremiumAmount());
        i.setIdv(dto.getIdv());
        i.setCoverageType(dto.getCoverageType());
        i.setName(dto.getName());
        i.setMobileNumber(dto.getMobileNumber());

        return map(insuranceRepo.save(i));
    }

    // =============================
    // DELETE
    // =============================
    @Override
    public void delete(Long id) {
        insuranceRepo.deleteById(id);
    }

    // =============================
    // UPLOAD DOCUMENT
    // =============================
    @Override
    public void uploadDocument(Long id, MultipartFile file) {

        Insurance insurance = insuranceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance not found"));

        try {
            String baseDir = System.getProperty("user.dir");

            // 🔥 delete old file
            if (insurance.getDocumentPath() != null) {
                Files.deleteIfExists(Paths.get(baseDir + "/" + insurance.getDocumentPath()));
            }

            // 🔥 validate
            String type = file.getContentType();
            if (type == null ||
                    !(type.equals("application/pdf") ||
                            type.equals("image/jpeg") ||
                            type.equals("image/png"))) {
                throw new RuntimeException("Only PDF, JPG, PNG allowed");
            }

            // 🔥 save
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String uploadDir = baseDir + "/uploads/insurance/";
            Files.createDirectories(Paths.get(uploadDir));

            Path path = Paths.get(uploadDir + fileName);
            Files.write(path, file.getBytes());

            insurance.setDocumentPath("uploads/insurance/" + fileName);
            insurance.setDocumentName(file.getOriginalFilename());
            insurance.setDocumentType(type);

            insuranceRepo.save(insurance);

        } catch (IOException e) {
            throw new RuntimeException("Upload failed", e);
        }
    }

    // =============================
    // DOWNLOAD DOCUMENT
    // =============================
    @Override
    public byte[] downloadDocument(Long id) {

        Insurance insurance = insuranceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance not found"));

        try {
            Path path = Paths.get(System.getProperty("user.dir") + "/" + insurance.getDocumentPath());
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException("Download failed");
        }
    }

    @Override
    public String getDocumentType(Long id) {
        return insuranceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"))
                .getDocumentType();
    }

    @Override
    public String getDocumentName(Long id) {
        return insuranceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"))
                .getDocumentName();
    }

    // =============================
    // REMINDERS
    // =============================
    @Override
    public List<InsuranceReminderDTO> getReminders(int thresholdDays) {

        List<Vehicle> vehicles = vehicleRepo.findAll();

        return vehicles.stream()
                .map(vehicle -> {

                    Insurance insurance = insuranceRepo
                            .findTopByVehicleIdOrderByExpiryDateDesc(vehicle.getId());

                    if (insurance == null) return null;

                    long remainingDays = ChronoUnit.DAYS.between(
                            LocalDate.now(),
                            insurance.getExpiryDate()
                    );

                    ReminderStatus status = calculateStatus(remainingDays, thresholdDays);

                    InsuranceReminderDTO dto = new InsuranceReminderDTO();
                    dto.setVehicleId(vehicle.getId());
                    dto.setVehicleNumber(vehicle.getVehicleNumber());
                    dto.setRemainingDays(remainingDays);
                    dto.setStatus(status);
                    dto.setExpiryDate(insurance.getExpiryDate());

                    return dto;
                })
                .filter(dto -> dto != null)
                .sorted((a, b) -> {

                    int p1 = getPriority(a.getStatus());
                    int p2 = getPriority(b.getStatus());

                    if (p1 != p2) return Integer.compare(p1, p2);

                    return Long.compare(a.getRemainingDays(), b.getRemainingDays());
                })
                .toList();
    }

    @Override
    public InsuranceReminderDTO getTopReminder(int thresholdDays) {
        return getReminders(thresholdDays).stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No reminders found"));
    }

    // =============================
    // HELPERS
    // =============================
    private ReminderStatus calculateStatus(long days, int threshold) {
        if (days <= 0) return ReminderStatus.OVERDUE;
        else if (days <= threshold) return ReminderStatus.DUE_SOON;
        else return ReminderStatus.SAFE;
    }

    private int getPriority(ReminderStatus status) {
        return switch (status) {
            case OVERDUE -> 1;
            case DUE_SOON -> 2;
            case SAFE -> 3;
        };
    }

    private InsuranceResponseDTO map(Insurance i) {

        InsuranceResponseDTO d = new InsuranceResponseDTO();

        d.setId(i.getId());
        d.setVehicleId(i.getVehicle().getId());
        d.setPolicyNumber(i.getPolicyNumber());
        d.setProvider(i.getProvider());
        d.setStartDate(i.getStartDate());
        d.setExpiryDate(i.getExpiryDate());
        d.setPremiumAmount(i.getPremiumAmount());
        d.setIdv(i.getIdv());
        d.setCoverageType(i.getCoverageType());

        d.setDocumentPath(i.getDocumentPath());
        d.setDocumentName(i.getDocumentName());
        d.setDocumentType(i.getDocumentType());
        d.setName(i.getName());
        d.setMobileNumber(i.getMobileNumber());

        d.setCreatedAt(i.getCreatedAt());
        d.setUpdatedAt(i.getUpdatedAt());

        return d;
    }

    @Override
    public InsuranceResponseDTO getLatestByVehicleId(Long vehicleId) {

        Insurance insurance = insuranceRepo
                .findTopByVehicleIdOrderByExpiryDateDesc(vehicleId);

        if (insurance == null) {
            throw new ResourceNotFoundException("No insurance found for vehicle");
        }

        return map(insurance);
    }
}