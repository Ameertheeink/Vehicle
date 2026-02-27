package com.vehicle.demo.controller;

import com.vehicle.demo.dto.*;
import com.vehicle.demo.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // ✅ Create Vehicle
    @PostMapping
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> createVehicle(
            @RequestBody VehicleRequestDTO requestDTO) {

        VehicleResponseDTO response = vehicleService.createVehicle(requestDTO);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Vehicle created successfully", response)
        );
    }

    // ✅ Get All Vehicles
    @GetMapping
    public ResponseEntity<ApiResponse<List<VehicleResponseDTO>>> getAllVehicles() {

        List<VehicleResponseDTO> vehicles = vehicleService.getAllVehicles();

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Vehicles fetched successfully", vehicles)
        );
    }

    // ✅ Get Vehicle By Id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> getVehicleById(
            @PathVariable Long id) {

        VehicleResponseDTO vehicle = vehicleService.getVehicleById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Vehicle fetched successfully", vehicle)
        );
    }

    // ✅ Update KM
    @PutMapping("/{id}/km")
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> updateVehicleKm(
            @PathVariable Long id,
            @RequestBody UpdateKmRequestDTO requestDTO) {

        VehicleResponseDTO updated = vehicleService.updateVehicleKm(id, requestDTO);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Vehicle KM updated successfully", updated)
        );
    }

    // ✅ Full Update
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponseDTO>> updateVehicle(
            @PathVariable Long id,
            @RequestBody VehicleUpdateRequestDTO requestDTO) {

        VehicleResponseDTO updated = vehicleService.updateVehicle(id, requestDTO);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Vehicle updated successfully", updated)
        );
    }

    // ✅ Delete Vehicle
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVehicle(@PathVariable Long id) {

        vehicleService.deleteVehicle(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Vehicle deleted successfully", null)
        );
    }

    // ✅ Upload Document
    @PostMapping("/{vehicleNumber}/document")
    public ResponseEntity<ApiResponse<Void>> uploadVehicleDocument(
            @PathVariable String vehicleNumber,
            @RequestParam("file") MultipartFile file) {

        vehicleService.uploadDocument(vehicleNumber, file);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Document uploaded successfully", null)
        );
    }

    // ✅ Download Document
    @GetMapping("/{vehicleNumber}/document")
    public ResponseEntity<byte[]> downloadVehicleDocument(
            @PathVariable String vehicleNumber) {

        byte[] fileData = vehicleService.getVehicleDocument(vehicleNumber);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=document.pdf")
                .header("Content-Type", "application/pdf")
                .body(fileData);
    }

    // ✅ Upload Images
    @PostMapping("/{id}/images")
    public ResponseEntity<ApiResponse<Void>> uploadVehicleImages(
            @PathVariable Long id,
            @RequestParam("files") MultipartFile[] files) {

        vehicleService.uploadVehicleImages(id, files);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Images uploaded successfully", null)
        );
    }

    // ✅ Get Images
    @GetMapping("/{id}/images")
    public ResponseEntity<ApiResponse<List<String>>> getVehicleImages(
            @PathVariable Long id) {

        List<String> images = vehicleService.getVehicleImages(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Images fetched successfully", images)
        );
    }

    // ✅ Delete Images
    @DeleteMapping("/{id}/images")
    public ResponseEntity<ApiResponse<Void>> deleteVehicleImages(
            @PathVariable Long id) {

        vehicleService.deleteImagesByVehicleId(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Images deleted successfully", null)
        );
    }
}
