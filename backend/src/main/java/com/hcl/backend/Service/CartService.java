package com.hcl.backend.service;

import com.hcl.backend.dto.CartDto;
import com.hcl.backend.dto.CartItem;

public interface CartService {
    CartDto getCart(String username);
    CartDto addItemToCart(String username, CartItem cartItem);
    CartDto updateItemInCart(String username, CartItem cartItem);
    CartDto removeItemFromCart(String username, Long productId);
    void clearCart(String username);
}
