package com.vehicle.demo.controller;

import com.vehicle.demo.dto.*;
import com.vehicle.demo.service.FitnessService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/fitness-certificates")
@RequiredArgsConstructor
public class FitnessController {

    private final FitnessService fitnessService;

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<ApiResponse<FitnessResponseDTO>> create(@RequestBody FitnessRequestDTO dto) {

        FitnessResponseDTO response = fitnessService.create(dto);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fitness created successfully", response)
        );
    }

    // ✅ GET BY VEHICLE
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<ApiResponse<FitnessResponseDTO>> getByVehicle(@PathVariable Long vehicleId) {

        FitnessResponseDTO response = fitnessService.getByVehicleId(vehicleId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fitness fetched successfully", response)
        );
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FitnessResponseDTO>> update(
            @PathVariable Long id,
            @RequestBody FitnessRequestDTO dto) {

        FitnessResponseDTO response = fitnessService.update(id, dto);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fitness updated successfully", response)
        );
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        fitnessService.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fitness deleted successfully", null)
        );
    }

    // ✅ UPLOAD DOCUMENT
    @PostMapping("/{id}/upload")
    public ResponseEntity<ApiResponse<String>> upload(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        fitnessService.uploadDocument(id, file);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "File uploaded successfully", null)
        );
    }

    // ✅ DOWNLOAD DOCUMENT (NO ApiResponse here ❗)
    @GetMapping("/{id}/document")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {

        byte[] file = fitnessService.downloadDocument(id);
        String contentType = fitnessService.getDocumentType(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        contentType != null ? contentType : "application/octet-stream"
                ))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(file);
    }

    // ✅ REMINDERS
    @GetMapping("/reminders")
    public ResponseEntity<ApiResponse<List<FitnessReminderDTO>>> reminders(
            @RequestParam(defaultValue = "10") int days) {

        List<FitnessReminderDTO> list = fitnessService.getReminders(days);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Reminder list fetched", list)
        );
    }
}