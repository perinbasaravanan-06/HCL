package com.hcl.backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "coupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private double discountValue;

    private double minOrderAmount;

    private double maxDiscount;

    private LocalDate expiryDate;

    private boolean isActive = true;

    private int usageLimit;

    private int usedCount;

    public enum DiscountType {
        FLAT,
        PERCENT
    }
}
