package com.hcl.backend.controller;

import com.hcl.backend.Entity.User;
import com.hcl.backend.dto.ApiResponse;
import com.hcl.backend.dto.CartDto;
import com.hcl.backend.dto.CartItem;
import com.hcl.backend.exception.ResourceNotFoundException;
import com.hcl.backend.repository.UserRepository;
import com.hcl.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse> getCart(@RequestParam Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        CartDto cart = cartService.getCart(user.getUsername());
        return new ResponseEntity<>(new ApiResponse(true, "Cart retrieved successfully", cart), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long userId, @RequestBody CartItem cartItem) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        CartDto cart = cartService.addItemToCart(user.getUsername(), cartItem);
        return new ResponseEntity<>(new ApiResponse(true, "Item added to cart successfully", cart), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateItemInCart(@RequestParam Long userId, @RequestBody CartItem cartItem) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        CartDto cart = cartService.updateItemInCart(user.getUsername(), cartItem);
        return new ResponseEntity<>(new ApiResponse(true, "Item updated in cart successfully", cart), HttpStatus.OK);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@RequestParam Long userId, @PathVariable Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        CartDto cart = cartService.removeItemFromCart(user.getUsername(), productId);
        return new ResponseEntity<>(new ApiResponse(true, "Item removed from cart successfully", cart), HttpStatus.OK);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse> clearCart(@RequestParam Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        cartService.clearCart(user.getUsername());
        return new ResponseEntity<>(new ApiResponse(true, "Cart cleared successfully", null), HttpStatus.OK);
    }
}
