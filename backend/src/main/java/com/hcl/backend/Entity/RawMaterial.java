package com.hcl.backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "raw_materials")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    private double quantityAvailable;

    private double reorderLevel = 10;

    private LocalDateTime lastUpdated = LocalDateTime.now();

    private String supplierName;

    public enum Unit {
        KG,
        LITRE,
        PIECE
    }
}
