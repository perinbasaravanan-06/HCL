package com.hcl.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    private Long productId;
    private String productName;
    private int quantity;
    private double price;
    private String size;
    private double subtotal;
}
