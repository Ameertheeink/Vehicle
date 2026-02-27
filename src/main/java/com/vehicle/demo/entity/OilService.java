package com.vehicle.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "oil_services")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OilService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(nullable = false)
    private Integer lastServiceKm;

    @Column(nullable = false)
    private Integer serviceIntervalKm;

    @Column(nullable = false)
    private LocalDate lastServiceDate;

    private String oilBrand;

    @Column(precision = 5, scale = 2)
    private BigDecimal oilQuantityLitres;

    private String serviceVendor;

    private String serviceBillPath;
    private String serviceBillName;
    private String serviceBillType;

    private Integer nextDueKm;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        calculateNextDueKm();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        calculateNextDueKm();
    }

    private void calculateNextDueKm() {
        if (lastServiceKm != null && serviceIntervalKm != null) {
            this.nextDueKm = lastServiceKm + serviceIntervalKm;
        }
    }
}
