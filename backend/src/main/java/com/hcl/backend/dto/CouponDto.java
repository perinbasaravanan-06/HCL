package com.hcl.backend.dto;

import com.hcl.backend.Entity.Coupon.DiscountType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CouponDto {
    private Long id;
    private String code;
    private DiscountType discountType;
    private double discountValue;
    private double minOrderAmount;
    private double maxDiscount;
    private LocalDate expiryDate;
    private boolean isActive;
    private int usageLimit;
    private int usedCount;
    private double discountAmount;
    private double finalAmount;
}
