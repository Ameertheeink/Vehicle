package com.vehicle.demo.service.impl;

import com.vehicle.demo.dto.TyreServiceReminderDTO;
import com.vehicle.demo.dto.TyreServiceRequestDTO;
import com.vehicle.demo.dto.TyreServiceResponseDTO;
import com.vehicle.demo.entity.TyreService;
import com.vehicle.demo.entity.Vehicle;
import com.vehicle.demo.enums.ReminderStatus;
import com.vehicle.demo.exception.ResourceNotFoundException;
import com.vehicle.demo.repository.TyreServiceRepository;
import com.vehicle.demo.repository.VehicleRepository;
import com.vehicle.demo.service.TyreServiceService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class TyreServiceServiceImpl implements TyreServiceService {

    private final TyreServiceRepository tyreRepo;
    private final VehicleRepository vehicleRepo;

    public TyreServiceServiceImpl(TyreServiceRepository tyreRepo,
                                  VehicleRepository vehicleRepo) {
        this.tyreRepo = tyreRepo;
        this.vehicleRepo = vehicleRepo;
    }

    @Override
    public TyreServiceResponseDTO create(TyreServiceRequestDTO dto) {

        Vehicle vehicle = vehicleRepo.findById(dto.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        TyreService t = new TyreService();
        t.setVehicle(vehicle);
        t.setChangeKm(dto.getChangeKm());
        t.setServiceIntervalKm(dto.getServiceIntervalKm());
        t.setChangeDate(dto.getChangeDate());
        t.setTyreBrand(dto.getTyreBrand());
        t.setTyreType(dto.getTyreType());
        t.setCost(dto.getCost());
        t.setVendor(dto.getVendor());

        TyreService saved = tyreRepo.save(t);

        return map(saved);
    }

    @Override
    public List<TyreServiceResponseDTO> getAll() {
        return tyreRepo.findAll().stream().map(this::map).toList();
    }

    @Override
    public List<TyreServiceResponseDTO> getByVehicleId(Long vehicleId) {
        return tyreRepo.findByVehicleId(vehicleId)
                .stream().map(this::map).toList();
    }

    @Override
    public TyreServiceResponseDTO getById(Long id) {
        return map(tyreRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found")));
    }

    @Override
    public TyreServiceResponseDTO update(Long id, TyreServiceRequestDTO dto) {

        TyreService t = tyreRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));

        t.setChangeKm(dto.getChangeKm());
        t.setServiceIntervalKm(dto.getServiceIntervalKm());
        t.setChangeDate(dto.getChangeDate());
        t.setTyreBrand(dto.getTyreBrand());
        t.setTyreType(dto.getTyreType());
        t.setCost(dto.getCost());
        t.setVendor(dto.getVendor());

        return map(tyreRepo.save(t));
    }

    @Override
    public void delete(Long id) {
        tyreRepo.deleteById(id);
    }

    // 🔥 Mapper
    private TyreServiceResponseDTO map(TyreService t) {

        TyreServiceResponseDTO d = new TyreServiceResponseDTO();

        d.setId(t.getId());
        d.setVehicleId(t.getVehicle().getId());
        d.setChangeKm(t.getChangeKm());
        d.setServiceIntervalKm(t.getServiceIntervalKm());
        d.setNextChangeKm(t.getNextChangeKm());
        d.setChangeDate(t.getChangeDate());
        d.setTyreBrand(t.getTyreBrand());
        d.setTyreType(t.getTyreType());
        d.setCost(t.getCost());
        d.setVendor(t.getVendor());

        d.setBillPath(t.getBillPath());
        d.setBillName(t.getBillName());
        d.setBillType(t.getBillType());

        d.setCreatedAt(t.getCreatedAt());
        d.setUpdatedAt(t.getUpdatedAt());

        return d;
    }
    @Override
    public void uploadBill(Long id, MultipartFile file) {

        TyreService service = tyreRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tyre service not found"));

        try {
            String baseDir = System.getProperty("user.dir");

            // 🔥 DELETE OLD FILE
            if (service.getBillPath() != null) {
                Files.deleteIfExists(Paths.get(baseDir + "/" + service.getBillPath()));
            }

            // 🔥 VALIDATION
            String contentType = file.getContentType();

            if (contentType == null ||
                    !(contentType.equals("application/pdf") ||
                            contentType.equals("image/jpeg") ||
                            contentType.equals("image/png"))) {

                throw new RuntimeException("Only PDF, JPEG, PNG allowed");
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            // 🔥 SAVE FILE
            String uploadDir = baseDir + "/uploads/tyre/";
            Files.createDirectories(Paths.get(uploadDir));

            Path path = Paths.get(uploadDir + fileName);
            Files.write(path, file.getBytes());

            // 🔥 UPDATE DB
            service.setBillPath("uploads/tyre/" + fileName);
            service.setBillName(file.getOriginalFilename());
            service.setBillType(contentType);

            tyreRepo.save(service);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload bill", e);
        }
    }

    @Override
    public byte[] downloadBill(Long id) {

        TyreService service = tyreRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tyre service not found"));

        try {
            Path path = Paths.get(System.getProperty("user.dir") + "/" + service.getBillPath());
            return Files.readAllBytes(path);

        } catch (IOException e) {
            throw new RuntimeException("Failed to download bill");
        }
    }
    @Override
    public String getBillType(Long id) {
        return tyreRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"))
                .getBillType();
    }

    @Override
    public String getBillName(Long id) {
        return tyreRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"))
                .getBillName();
    }

    @Override
    public List<TyreServiceReminderDTO> getReminders(int threshold) {

        List<Vehicle> vehicles = vehicleRepo.findAll();

        return vehicles.stream()
                .map(vehicle -> {

                    TyreService service = tyreRepo
                            .findTopByVehicleIdOrderByChangeDateDesc(vehicle.getId());

                    if (service == null) return null;

                    int remainingKm =
                            service.getNextChangeKm() - vehicle.getCurrentKm();

                    ReminderStatus status = calculateStatus(remainingKm, threshold);

                    TyreServiceReminderDTO dto = new TyreServiceReminderDTO();
                    dto.setVehicleId(vehicle.getId());
                    dto.setVehicleNumber(vehicle.getVehicleNumber());
                    dto.setRemainingKm(remainingKm);
                    dto.setStatus(status);

                    return dto;
                })
                .filter(dto -> dto != null)
                .sorted((a, b) -> {

                    int p1 = getPriority(a.getStatus());
                    int p2 = getPriority(b.getStatus());

                    if (p1 != p2) return Integer.compare(p1, p2);

                    return Integer.compare(a.getRemainingKm(), b.getRemainingKm());
                })
                .toList();
    }
    private ReminderStatus calculateStatus(int remainingKm, int threshold) {
        if (remainingKm <= 0) return ReminderStatus.OVERDUE;
        else if (remainingKm <= threshold) return ReminderStatus.DUE_SOON;
        else return ReminderStatus.SAFE;
    }

    private int getPriority(ReminderStatus status) {
        return switch (status) {
            case OVERDUE -> 1;
            case DUE_SOON -> 2;
            case SAFE -> 3;
        };
    }
    @Override
    public TyreServiceReminderDTO getTopReminder(int threshold) {

        List<TyreServiceReminderDTO> reminders = getReminders(threshold);

        if (reminders.isEmpty()) {
            throw new RuntimeException("No tyre reminders available");
        }

        return reminders.get(0); // 🔥 highest priority
    }
}