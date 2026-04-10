package com.vehicle.demo.controller;

import com.vehicle.demo.dto.TyreServiceReminderDTO;
import com.vehicle.demo.dto.TyreServiceRequestDTO;
import com.vehicle.demo.dto.TyreServiceResponseDTO;
import com.vehicle.demo.service.TyreServiceService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.vehicle.demo.dto.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/tyre-services")
public class TyreServiceController {

    private final TyreServiceService service;

    public TyreServiceController(TyreServiceService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TyreServiceResponseDTO>> create(
            @RequestBody TyreServiceRequestDTO dto) {

        TyreServiceResponseDTO response = service.create(dto);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Tyre service created successfully", response)
        );
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<TyreServiceResponseDTO>>> getAll() {

        List<TyreServiceResponseDTO> list = service.getAll();

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Tyre services fetched successfully", list)
        );
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<ApiResponse<List<TyreServiceResponseDTO>>> getByVehicle(
            @PathVariable Long vehicleId) {

        List<TyreServiceResponseDTO> list =
                service.getByVehicleId(vehicleId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Tyre services fetched successfully", list)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TyreServiceResponseDTO>> update(
            @PathVariable Long id,
            @RequestBody TyreServiceRequestDTO dto) {

        TyreServiceResponseDTO response = service.update(id, dto);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Tyre service updated successfully", response)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Tyre service deleted successfully", null)
        );
    }

    @PostMapping("/{id}/bill")
    public ResponseEntity<ApiResponse<String>> uploadBill(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        service.uploadBill(id, file);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Bill uploaded successfully", null)
        );
    }
    @GetMapping("/{id}/bill")
    public ResponseEntity<byte[]> downloadBill(@PathVariable Long id) {

        byte[] data = service.downloadBill(id);

        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=" + service.getBillName(id))
                .header("Content-Type", service.getBillType(id))
                .body(data);
    }
    @GetMapping("/reminders")
    public ResponseEntity<ApiResponse<List<TyreServiceReminderDTO>>> getReminders(
            @RequestParam(defaultValue = "5000") int threshold) {

        List<TyreServiceReminderDTO> list =
                service.getReminders(threshold);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Reminders fetched successfully", list)
        );
    }

    @GetMapping("/reminders/top")
    public ResponseEntity<ApiResponse<TyreServiceReminderDTO>> getTopReminder(
            @RequestParam(defaultValue = "5000") int threshold) {

        TyreServiceReminderDTO dto =
                service.getTopReminder(threshold);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Top reminder fetched successfully", dto)
        );
    }

    @GetMapping("/vehicle/{vehicleId}/latest")
    public ResponseEntity<ApiResponse<TyreServiceResponseDTO>> getLatestTyreService(
            @PathVariable Long vehicleId) {

        TyreServiceResponseDTO response =
                service.getLatestTyreService(vehicleId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Latest tyre service fetched successfully", response)
        );
    }
    @GetMapping("/paginated")
    public ResponseEntity<?> getPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<TyreServiceResponseDTO> result =
                service.getAllPaginated(page, size);

        return ResponseEntity.ok(result);
    }
    @GetMapping("/vehicle/{vehicleId}/paginated")
    public ResponseEntity<?> getByVehiclePaginated(
            @PathVariable Long vehicleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<TyreServiceResponseDTO> result =
                service.getByVehiclePaginated(vehicleId, page, size);

        return ResponseEntity.ok(result);
    }
}