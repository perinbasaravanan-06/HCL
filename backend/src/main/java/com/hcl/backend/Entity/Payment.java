package com.hcl.backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stripePaymentIntentId;

    private double amount;

    private String currency;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String paymentMethod;

    private LocalDateTime paidAt;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public enum PaymentStatus {
        PENDING,
        SUCCESS,
        FAILED
    }
}
