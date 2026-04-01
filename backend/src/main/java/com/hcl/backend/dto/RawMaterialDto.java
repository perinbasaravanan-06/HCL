package com.hcl.backend.dto;

import com.hcl.backend.Entity.RawMaterial.Unit;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RawMaterialDto {
    private Long id;
    private String name;
    private Unit unit;
    private double quantityAvailable;
    private double reorderLevel;
    private LocalDateTime lastUpdated;
    private String supplierName;
}
