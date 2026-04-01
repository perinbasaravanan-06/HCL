package com.hcl.backend.dto;

import lombok.Data;

@Data
public class PlaceOrderDto {
    private String paymentMethod;
    private String couponCode;
}
