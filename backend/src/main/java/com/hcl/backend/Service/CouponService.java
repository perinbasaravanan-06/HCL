package com.hcl.backend.service;

import com.hcl.backend.dto.CouponDto;

import java.util.List;

public interface CouponService {
    CouponDto createCoupon(CouponDto couponDto);
    CouponDto updateCoupon(Long id, CouponDto couponDto);
    void deleteCoupon(Long id);
    List<CouponDto> getAllCoupons();
    CouponDto validateAndApplyCoupon(String code, double orderAmount);
}
