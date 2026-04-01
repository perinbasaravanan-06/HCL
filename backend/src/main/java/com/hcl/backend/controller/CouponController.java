package com.hcl.backend.controller;

import com.hcl.backend.dto.ApiResponse;
import com.hcl.backend.dto.CouponDto;
import com.hcl.backend.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping
    public ResponseEntity<ApiResponse> createCoupon(@RequestBody CouponDto couponDto) {
        CouponDto newCoupon = couponService.createCoupon(couponDto);
        return new ResponseEntity<>(new ApiResponse(true, "Coupon created successfully", newCoupon), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCoupon(@PathVariable Long id, @RequestBody CouponDto couponDto) {
        CouponDto updatedCoupon = couponService.updateCoupon(id, couponDto);
        return new ResponseEntity<>(new ApiResponse(true, "Coupon updated successfully", updatedCoupon), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return new ResponseEntity<>(new ApiResponse(true, "Coupon deleted successfully", null), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllCoupons() {
        List<CouponDto> coupons = couponService.getAllCoupons();
        return new ResponseEntity<>(new ApiResponse(true, "Coupons retrieved successfully", coupons), HttpStatus.OK);
    }

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse> applyCoupon(@RequestParam String code, @RequestParam double orderAmount) {
        CouponDto couponDto = couponService.validateAndApplyCoupon(code, orderAmount);
        return new ResponseEntity<>(new ApiResponse(true, "Coupon applied successfully", couponDto), HttpStatus.OK);
    }
}
