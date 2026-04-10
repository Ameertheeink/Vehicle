package com.vehicle.demo.controller;



import com.vehicle.demo.dto.*;
import com.vehicle.demo.service.InsuranceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/insurance")
public class InsuranceController {

    private final InsuranceService service;

    public InsuranceController(InsuranceService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<ApiResponse<InsuranceResponseDTO>> create(
            @RequestBody InsuranceRequestDTO dto) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Insurance created", service.create(dto))
        );
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<ApiResponse<List<InsuranceResponseDTO>>> getAll() {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fetched", service.getAll())
        );
    }

    // GET BY VEHICLE
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<ApiResponse<List<InsuranceResponseDTO>>> getByVehicle(
            @PathVariable Long vehicleId) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fetched", service.getByVehicleId(vehicleId))
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InsuranceResponseDTO>> update(
            @PathVariable Long id,
            @RequestBody InsuranceRequestDTO dto) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Updated", service.update(id, dto))
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Deleted", null)
        );
    }

    // UPLOAD DOCUMENT
    @PostMapping("/{id}/document")
    public ResponseEntity<ApiResponse<String>> upload(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        service.uploadDocument(id, file);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Uploaded", null)
        );
    }

    // DOWNLOAD DOCUMENT
    @GetMapping("/{id}/document")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {

        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "inline; filename=" + service.getDocumentName(id))
                .header("Content-Type", service.getDocumentType(id))
                .body(service.downloadDocument(id));
    }

    // REMINDERS
    @GetMapping("/reminders")
    public ResponseEntity<ApiResponse<List<InsuranceReminderDTO>>> reminders(
            @RequestParam(defaultValue = "10") int thresholdDays) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Reminders", service.getReminders(thresholdDays))
        );
    }

    // TOP REMINDER
    @GetMapping("/reminders/top")
    public ResponseEntity<ApiResponse<InsuranceReminderDTO>> topReminder(
            @RequestParam(defaultValue = "10") int thresholdDays) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Top Reminder",
                        service.getTopReminder(thresholdDays))
        );
    }

    @GetMapping("/vehicle/{vehicleId}/latest")
    public ResponseEntity<ApiResponse<InsuranceResponseDTO>> getLatest(
            @PathVariable Long vehicleId) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Latest insurance fetched",
                        service.getLatestByVehicleId(vehicleId))
        );
    }
}