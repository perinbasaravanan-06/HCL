package com.hcl.backend.dto;

import com.hcl.backend.Entity.Payment.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentDto {
    private Long id;
    private String stripePaymentIntentId;
    private double amount;
    private String currency;
    private PaymentStatus status;
    private String paymentMethod;
    private LocalDateTime paidAt;
    private Long orderId;
}
