package com.vehicle.demo.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_registrations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Relation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    private String registrationNumber;
    private LocalDate registrationDate;

    private String chassisNumber;
    private String engineNumber;

    private String registrationAuthority;
    private String makersName;
    private String modelName;

    private String vehicleClass;
    private String bodyType;
    private String colour;
    private String fuelType;

    private String emissionNorms;
    private String manufacturingMonthYear;

    private Integer cubicCapacity;
    private Integer seatingCapacity;

    private String ownerName;
    private String fatherOrHusbandName;

    private String address;
    private String pinCode;
    private String ownerSerialNumber;

    // 📄 Document
    private String documentPath;
    private String documentName;
    private String documentType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}