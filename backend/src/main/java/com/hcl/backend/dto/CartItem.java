package com.hcl.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private Long productId;
    private String productName;
    private int quantity;
    private Double price;
    private String size;
    private Double subtotal;
    private String imageUrl;
}
