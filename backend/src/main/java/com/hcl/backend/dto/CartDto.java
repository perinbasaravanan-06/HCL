package com.hcl.backend.dto;

import com.hcl.backend.Entity.Cart.CartStatus;
import lombok.Data;

import java.util.List;

@Data
public class CartDto {
    private Long id;
    private Long userId;
    private List<CartItem> items;
    private double totalAmount;
    private int totalItems;
    private CartStatus status;
}
