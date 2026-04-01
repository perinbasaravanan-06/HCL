package com.hcl.backend.service.impl;

import com.hcl.backend.Entity.Coupon;
import com.hcl.backend.dto.CouponDto;
import com.hcl.backend.exception.CouponInvalidException;
import com.hcl.backend.exception.ResourceNotFoundException;
import com.hcl.backend.repository.CouponRepository;
import com.hcl.backend.service.CouponService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Override
    public CouponDto createCoupon(CouponDto couponDto) {
        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(couponDto, coupon);
        Coupon savedCoupon = couponRepository.save(coupon);
        return convertToDto(savedCoupon);
    }

    @Override
    public CouponDto updateCoupon(Long id, CouponDto couponDto) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));
        BeanUtils.copyProperties(couponDto, coupon);
        coupon.setId(id);
        Coupon updatedCoupon = couponRepository.save(coupon);
        return convertToDto(updatedCoupon);
    }

    @Override
    public void deleteCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));
        couponRepository.delete(coupon);
    }

    @Override
    public List<CouponDto> getAllCoupons() {
        return couponRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CouponDto validateAndApplyCoupon(String code, double orderAmount) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new CouponInvalidException("Invalid coupon code"));

        if (!coupon.isActive() || coupon.getExpiryDate().isBefore(LocalDate.now()) || coupon.getUsedCount() >= coupon.getUsageLimit()) {
            throw new CouponInvalidException("Coupon is not valid");
        }

        if (orderAmount < coupon.getMinOrderAmount()) {
            throw new CouponInvalidException("Minimum order amount not met");
        }

        double discount = 0;
        if (coupon.getDiscountType() == Coupon.DiscountType.FLAT) {
            discount = coupon.getDiscountValue();
        } else {
            discount = (orderAmount * coupon.getDiscountValue()) / 100;
            if (discount > coupon.getMaxDiscount()) {
                discount = coupon.getMaxDiscount();
            }
        }

        CouponDto couponDto = convertToDto(coupon);
        couponDto.setDiscountAmount(discount);
        couponDto.setFinalAmount(orderAmount - discount);

        return couponDto;
    }

    private CouponDto convertToDto(Coupon coupon) {
        CouponDto couponDto = new CouponDto();
        BeanUtils.copyProperties(coupon, couponDto);
        return couponDto;
    }
}
