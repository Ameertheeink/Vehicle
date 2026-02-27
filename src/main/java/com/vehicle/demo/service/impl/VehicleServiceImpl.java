package com.vehicle.demo.service.impl;

import com.vehicle.demo.dto.UpdateKmRequestDTO;
import com.vehicle.demo.dto.VehicleRequestDTO;
import com.vehicle.demo.dto.VehicleResponseDTO;
import com.vehicle.demo.dto.VehicleUpdateRequestDTO;
import com.vehicle.demo.entity.Vehicle;
import com.vehicle.demo.entity.VehicleImage;
import com.vehicle.demo.exception.ResourceNotFoundException;
import com.vehicle.demo.mapper.VehicleMapper;
import com.vehicle.demo.repository.VehicleImageRepository;
import com.vehicle.demo.repository.VehicleRepository;
import com.vehicle.demo.service.VehicleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleImageRepository vehicleImageRepository;

    private final VehicleMapper vehicleMapper;

    @Override
    public VehicleResponseDTO createVehicle(VehicleRequestDTO requestDTO) {

        Vehicle vehicle = vehicleMapper.toEntity(requestDTO);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toResponseDTO(savedVehicle);
    }

    @Override
    public List<VehicleResponseDTO> getAllVehicles() {

        List<Vehicle> vehicles = vehicleRepository.findAll();

        return vehicles.stream()
                .map(vehicleMapper::toResponseDTO)
                .toList();
    }


    @Override
    public VehicleResponseDTO updateVehicleKm(Long id, UpdateKmRequestDTO requestDTO) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));

        vehicle.setCurrentKm(requestDTO.getCurrentKm());
        vehicle.setUpdatedBy(requestDTO.getUpdatedBy());

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toResponseDTO(updatedVehicle);
    }

    @Override
    public VehicleResponseDTO updateVehicleKm(String vehicleNumber,
                                              UpdateKmRequestDTO requestDTO) {

        Vehicle vehicle = vehicleRepository
                .findByVehicleNumber(vehicleNumber)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        if (requestDTO.getCurrentKm() < vehicle.getCurrentKm()) {
            throw new RuntimeException("KM cannot decrease");
        }

        vehicle.setCurrentKm(requestDTO.getCurrentKm());
        vehicle.setUpdatedBy(requestDTO.getUpdatedBy());

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toResponseDTO(updatedVehicle);
    }

    @Override
    public void uploadDocument(String vehicleNumber, MultipartFile file) {

        Vehicle vehicle = vehicleRepository
                .findByVehicleNumber(vehicleNumber)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        try {
            vehicle.setDocument(file.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Error saving file", e);
        }

        vehicleRepository.save(vehicle);
    }

    @Override
    public byte[] getVehicleDocument(String vehicleNumber) {

        Vehicle vehicle = vehicleRepository
                .findByVehicleNumber(vehicleNumber)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        return vehicle.getDocument();
    }
    @Override
    public VehicleResponseDTO updateVehicle(Long id, VehicleUpdateRequestDTO requestDTO) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        vehicle.setOwnerName(requestDTO.getOwnerName());
        vehicle.setVehicleType(requestDTO.getVehicleType());
        vehicle.setBrand(requestDTO.getBrand());
        vehicle.setModel(requestDTO.getModel());
        vehicle.setFuelType(requestDTO.getFuelType());
        vehicle.setManufacturingYear(requestDTO.getManufacturingYear());
        vehicle.setColor(requestDTO.getColor());
        vehicle.setCurrentKm(requestDTO.getCurrentKm());
        vehicle.setUpdatedBy(requestDTO.getUpdatedBy());

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toResponseDTO(updatedVehicle);
    }
     @Override
    public void deleteVehicle(Long id) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));

        vehicleRepository.delete(vehicle);
    }
    @Override
    @Transactional
    public void uploadVehicleImages(Long id, MultipartFile[] files) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        String uploadDir = System.getProperty("user.dir") + "/uploads/";

        try {
            Files.createDirectories(Paths.get(uploadDir));

            for (MultipartFile file : files) {

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);

                Files.write(filePath, file.getBytes());

                VehicleImage image = new VehicleImage();
                image.setImagePath(fileName);
                image.setVehicle(vehicle);

                vehicleImageRepository.save(image);
            }

        } catch (Exception e) {
            e.printStackTrace();   // 🔥 show real error in console
            throw new RuntimeException("Image upload failed", e);
        }
    }

    @Override
    public VehicleResponseDTO getVehicleById(Long id) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        return vehicleMapper.toResponseDTO(vehicle);
    }
    public List<String> getVehicleImages(Long vehicleId) {

        List<VehicleImage> images = vehicleImageRepository.findByVehicleId(vehicleId);

        return images.stream()
                .map(VehicleImage::getImagePath)
                .toList();
    }
    @Override
    public void deleteImagesByVehicleId(Long vehicleId) {

        // 1️⃣ Get all images from DB
        List<VehicleImage> images = vehicleImageRepository.findByVehicleId(vehicleId);

        if (images.isEmpty()) {
            throw new ResourceNotFoundException("No images found for vehicle id: " + vehicleId);
        }

        // 2️⃣ Delete physical files
        for (VehicleImage image : images) {
            try {
                Path path = Paths.get(image.getImagePath());
                Files.deleteIfExists(path);
            } catch (Exception e) {
                System.out.println("Failed to delete file: " + image.getImagePath());
            }
        }

        // 3️⃣ Delete DB records
        vehicleImageRepository.deleteAll(images);
    }



}