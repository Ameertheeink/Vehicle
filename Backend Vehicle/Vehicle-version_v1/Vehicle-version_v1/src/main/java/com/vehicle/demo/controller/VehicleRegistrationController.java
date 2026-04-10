package com.vehicle.demo.controller;

import com.vehicle.demo.dto.ApiResponse;
import com.vehicle.demo.dto.VehicleRegistrationRequestDTO;
import com.vehicle.demo.dto.VehicleRegistrationResponseDTO;
import com.vehicle.demo.service.VehicleRegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle-registrations")
public class VehicleRegistrationController {

    private final VehicleRegistrationService service;

    public VehicleRegistrationController(VehicleRegistrationService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<ApiResponse<VehicleRegistrationResponseDTO>> create(
            @RequestBody VehicleRegistrationRequestDTO dto) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "RC created", service.create(dto))
        );
    }

    // GET BY VEHICLE
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<ApiResponse<List<VehicleRegistrationResponseDTO>>> getByVehicle(
            @PathVariable Long vehicleId) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fetched", service.getByVehicleId(vehicleId))
        );
    }

    // GET LATEST RC
    @GetMapping("/vehicle/{vehicleId}/latest")
    public ResponseEntity<ApiResponse<VehicleRegistrationResponseDTO>> getLatest(
            @PathVariable Long vehicleId) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Latest RC",
                        service.getLatestByVehicleId(vehicleId))
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleRegistrationResponseDTO>> update(
            @PathVariable Long id,
            @RequestBody VehicleRegistrationRequestDTO dto) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Updated", service.update(id, dto))
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Deleted", null)
        );
    }

    // UPLOAD DOCUMENT
    @PostMapping("/{id}/document")
    public ResponseEntity<ApiResponse<Void>> upload(
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
                        "attachment; filename=" + service.getDocumentName(id))
                .header("Content-Type", service.getDocumentType(id))
                .body(service.downloadDocument(id));
    }
}