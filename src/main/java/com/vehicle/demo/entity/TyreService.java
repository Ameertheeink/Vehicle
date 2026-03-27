package com.vehicle.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tyre_services")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TyreService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Relation
    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;
    @Column(nullable = false)
    private Integer changeKm;
    @Column(nullable = false)
    private Integer serviceIntervalKm;
    @Column(nullable = false)
    private Integer nextChangeKm;
    @Column(nullable = false)
    private LocalDate changeDate;
    @Column(nullable = false)
    private String tyreBrand;
    @Column(nullable = false)
    private String tyreType;

    @Column(nullable = false)
    private Double cost;
    @Column(nullable = false)
    private String vendor;

    // 📄 Bill
    private String billPath;
    private String billName;
    private String billType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 🔥 Auto calculation


    // 🔥 Auto timestamps


    @PrePersist
    @PreUpdate
    public void preSaveOrUpdate() {

        // 🔥 calculate next KM
        if (changeKm != null && serviceIntervalKm != null) {
            this.nextChangeKm = changeKm + serviceIntervalKm;
        }

        // 🔥 timestamps
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        updatedAt = LocalDateTime.now();
    }

    // getters & setters
}