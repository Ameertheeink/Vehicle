package com.vehicle.demo.controller;

import com.vehicle.demo.dto.ApiResponse;
import com.vehicle.demo.dto.OilServiceReminderDTO;
import com.vehicle.demo.dto.OilServiceRequestDTO;
import com.vehicle.demo.dto.OilServiceResponseDTO;
import com.vehicle.demo.service.OilServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/oil-services")
public class OilServiceController {

    private static final Logger logger =
            LoggerFactory.getLogger(OilServiceController.class);

    private final OilServiceService oilServiceService;

    public OilServiceController(OilServiceService oilServiceService) {
        this.oilServiceService = oilServiceService;
    }

    // =========================================================
    // CREATE OIL SERVICE
    // =========================================================
    @PostMapping
    public ResponseEntity<ApiResponse<OilServiceResponseDTO>> createOilService(
            @RequestBody OilServiceRequestDTO requestDTO) {

        logger.info("Creating oil service for vehicleId: {}", requestDTO.getVehicleId());

        OilServiceResponseDTO response =
                oilServiceService.createOilService(requestDTO);

        logger.info("Oil service created successfully with id: {}", response.getId());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Oil service created successfully", response)
        );
    }

    // =========================================================
    // GET OIL SERVICES BY VEHICLE ID
    // =========================================================
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<ApiResponse<List<OilServiceResponseDTO>>> getByVehicleId(
            @PathVariable Long vehicleId) {

        logger.info("Fetching oil services for vehicleId: {}", vehicleId);

        List<OilServiceResponseDTO> services =
                oilServiceService.getOilServicesByVehicleId(vehicleId);

        logger.info("Found {} oil services for vehicleId: {}", services.size(), vehicleId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Oil services fetched successfully", services)
        );
    }

    // =========================================================
    // UPLOAD SERVICE BILL
    // =========================================================
    @PostMapping("/{id}/bill")
    public ResponseEntity<ApiResponse<String>> uploadServiceBill(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        logger.info("Uploading bill for oilServiceId: {}", id);
        logger.debug("Uploaded file name: {}, size: {} bytes",
                file.getOriginalFilename(), file.getSize());

        oilServiceService.uploadServiceBill(id, file);

        logger.info("Bill uploaded successfully for oilServiceId: {}", id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Service bill uploaded successfully", null)
        );
    }

    // =========================================================
    // DOWNLOAD SERVICE BILL
    // =========================================================
    @GetMapping("/{id}/bill")
    public ResponseEntity<byte[]> downloadBill(@PathVariable Long id) {

        logger.info("Downloading bill for oilServiceId: {}", id);

        byte[] fileData = oilServiceService.downloadServiceBill(id);
        String contentType = oilServiceService.getServiceBillType(id);
        String fileName = oilServiceService.getServiceBillName(id);

        logger.info("Bill download initiated for oilServiceId: {}", id);

        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=" + fileName)
                .header("Content-Type", contentType)
                .body(fileData);
    }

    // =========================================================
    // GET ALL OIL SERVICES
    // =========================================================
    @GetMapping
    public ResponseEntity<List<OilServiceResponseDTO>> getAllOilServices() {

        logger.info("Fetching all oil services");

        List<OilServiceResponseDTO> list =
                oilServiceService.getAllOilServices();

        logger.info("Total oil services found: {}", list.size());

        return ResponseEntity.ok(list);
    }

    // =========================================================
    // DELETE BILL
    // =========================================================
    @DeleteMapping("/{id}/bill")
    public ResponseEntity<?> deleteBill(@PathVariable Long id) {

        logger.info("Deleting bill for oilServiceId: {}", id);

        oilServiceService.deleteBill(id);

        logger.info("Bill deleted successfully for oilServiceId: {}", id);

        return ResponseEntity.ok(
                Map.of("message", "Bill deleted successfully")
        );
    }

    // =========================================================
    // UPDATE OIL SERVICE
    // =========================================================
    @PutMapping("/{id}")
    public ResponseEntity<OilServiceResponseDTO> updateOilService(
            @PathVariable Long id,
            @RequestBody OilServiceRequestDTO requestDTO) {

        logger.info("Updating oil service with id: {}", id);

        OilServiceResponseDTO response =
                oilServiceService.updateOilService(id, requestDTO);

        logger.info("Oil service updated successfully with id: {}", id);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // DELETE OIL SERVICE
    // =========================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOilService(@PathVariable Long id) {

        logger.warn("Deleting oil service with id: {}", id);

        oilServiceService.deleteOilService(id);

        logger.info("Oil service deleted successfully with id: {}", id);

        return ResponseEntity.ok(
                Map.of("message", "Oil service deleted successfully")
        );
    }

    // =========================================================
// GET OIL SERVICE REMINDERS
// =========================================================
    @GetMapping("/reminders")
    public ResponseEntity<ApiResponse<List<OilServiceReminderDTO>>> getReminders(
            @RequestParam(defaultValue = "500") int threshold) {

        logger.info("Fetching oil service reminders with threshold: {}", threshold);

        List<OilServiceReminderDTO> reminders =
                oilServiceService.getReminders(threshold);

        logger.info("Total reminders found: {}", reminders.size());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Reminders fetched successfully", reminders)
        );
    }
    // =========================================================
// GET TOP PRIORITY REMINDER
// =========================================================
    @GetMapping("/reminders/top")
    public ResponseEntity<ApiResponse<OilServiceReminderDTO>> getTopReminder(
            @RequestParam(defaultValue = "500") int threshold) {

        logger.info("Fetching top priority reminder");

        OilServiceReminderDTO reminder =
                oilServiceService.getReminders(threshold)
                        .stream()
                        .findFirst()
                        .orElse(null);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Top reminder fetched successfully", reminder)
        );
    }
}
