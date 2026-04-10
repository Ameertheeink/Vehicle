package com.vehicle.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "pollution_certificates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PollutionCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Vehicle relation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(nullable = false)
    private String certificateNumber;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate expiryDate;

    private Double cost;
    private String vendor;

    // 📄 Document
    private String documentPath;
    private String documentName;
    private String documentType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ✅ Auto timestamps
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