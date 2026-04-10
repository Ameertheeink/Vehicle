package com.vehicle.demo.controller;

import com.vehicle.demo.dto.*;
import com.vehicle.demo.service.PollutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/pollution")
public class PollutionController {

    private final PollutionService service;

    public PollutionController(PollutionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PollutionResponseDTO>> create(
            @RequestBody PollutionRequestDTO dto) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Created", service.create(dto))
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PollutionResponseDTO>>> getAll() {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fetched", service.getAll())
        );
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<ApiResponse<List<PollutionResponseDTO>>> getByVehicle(
            @PathVariable Long vehicleId) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fetched",
                        service.getByVehicleId(vehicleId))
        );
    }

    @GetMapping("/vehicle/{vehicleId}/latest")
    public ResponseEntity<ApiResponse<PollutionResponseDTO>> getLatestByVehicleId(
            @PathVariable Long vehicleId) {

        PollutionResponseDTO response =
                service.getLatestByVehicleId(vehicleId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Latest pollution fetched successfully", response)
        );
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PollutionResponseDTO>> update(
            @PathVariable Long id,
            @RequestBody PollutionRequestDTO dto) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Updated", service.update(id, dto))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Deleted", null)
        );
    }

    // 📄 Upload
    @PostMapping("/{id}/document")
    public ResponseEntity<ApiResponse<Void>> upload(
            @PathVariable Long id,
            @RequestParam MultipartFile file) {

        service.uploadDocument(id, file);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Uploaded", null)
        );
    }

    // 📄 Download / View
    @GetMapping("/{id}/document")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {

        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=" + service.getDocumentName(id))
                .header("Content-Type", service.getDocumentType(id))
                .body(service.downloadDocument(id));
    }

    // 🔔 Reminders
    @GetMapping("/reminders")
    public ResponseEntity<ApiResponse<List<PollutionReminderDTO>>> reminders(
            @RequestParam(defaultValue = "7") int thresholdDays) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Reminders",
                        service.getReminders(thresholdDays))
        );
    }

    // 🔥 Top priority
    @GetMapping("/reminders/top")
    public ResponseEntity<ApiResponse<PollutionReminderDTO>> topReminder(
            @RequestParam(defaultValue = "7") int thresholdDays) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Top Reminder",
                        service.getTopReminder(thresholdDays))
        );
    }
    @PutMapping("/vehicle/{vehicleId}")
    public ResponseEntity<ApiResponse<PollutionResponseDTO>> updateLatest(
            @PathVariable Long vehicleId,
            @RequestBody PollutionRequestDTO dto) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Updated successfully",
                        service.updateLatestByVehicleId(vehicleId, dto))
        );
    }
}