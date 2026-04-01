package com.hcl.backend.dto;

import com.hcl.backend.Entity.Order.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private String userName;
    private List<OrderItem> items;
    private OrderStatus status;
    private double totalAmount;
    private Long deliveryAddressId;
    private String paymentMethod;
    private LocalDateTime orderDate;
}
