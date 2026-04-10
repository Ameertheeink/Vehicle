package com.vehicle.demo.entity;



import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@Table(name = "fitness_certificate",
        uniqueConstraints = @UniqueConstraint(columnNames = "vehicleId"))
public class FitnessCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long vehicleId;

    private LocalDate issueDate;
    private LocalDate expiryDate;

    private String documentPath;
    private String documentType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}