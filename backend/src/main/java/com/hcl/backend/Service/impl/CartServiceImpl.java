package com.hcl.backend.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.backend.Entity.Cart;
import com.hcl.backend.Entity.Product;
import com.hcl.backend.Entity.User;
import com.hcl.backend.dto.CartDto;
import com.hcl.backend.dto.CartItem;
import com.hcl.backend.exception.OutOfStockException;
import com.hcl.backend.exception.ResourceNotFoundException;
import com.hcl.backend.repository.CartRepository;
import com.hcl.backend.repository.ProductRepository;
import com.hcl.backend.repository.UserRepository;
import com.hcl.backend.service.CartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public CartDto getCart(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(() -> createNewCart(user));
        return convertToDto(cart);
    }

    @Override
    public CartDto addItemToCart(String username, CartItem cartItem) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(() -> createNewCart(user));
        Product product = productRepository.findById(cartItem.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getStock() < cartItem.getQuantity()) {
            throw new OutOfStockException("Product is out of stock");
        }

        List<CartItem> items = getCartItems(cart);
        // Check if item already exists in cart
        CartItem existingItem = items.stream()
                .filter(item -> item.getProductId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
            existingItem.setSubtotal(existingItem.getPrice() * existingItem.getQuantity());
        } else {
            CartItem resolvedItem = new CartItem();
            resolvedItem.setProductId(product.getId());
            resolvedItem.setProductName(product.getName());
            resolvedItem.setQuantity(cartItem.getQuantity());
            resolvedItem.setSize(product.getSize());
            resolvedItem.setPrice(product.getPrice());
            resolvedItem.setSubtotal(product.getPrice() * cartItem.getQuantity());
            resolvedItem.setImageUrl(product.getImageUrl());
            items.add(resolvedItem);
        }

        updateCartItems(cart, items);
        return convertToDto(cartRepository.save(cart));
    }

    @Override
    public CartDto updateItemInCart(String username, CartItem cartItem) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        List<CartItem> items = getCartItems(cart);
        Product product = productRepository.findById(cartItem.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getStock() < cartItem.getQuantity()) {
            throw new OutOfStockException("Product is out of stock");
        }

        items.stream()
                .filter(item -> item.getProductId().equals(cartItem.getProductId()))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(cartItem.getQuantity());
                    item.setPrice(product.getPrice());
                    item.setSubtotal(product.getPrice() * cartItem.getQuantity());
                });
        updateCartItems(cart, items);
        return convertToDto(cartRepository.save(cart));
    }

    @Override
    public CartDto removeItemFromCart(String username, Long productId) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        List<CartItem> items = getCartItems(cart);
        items.removeIf(item -> item.getProductId().equals(productId));
        updateCartItems(cart, items);
        return convertToDto(cartRepository.save(cart));
    }

    @Override
    public void clearCart(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        cart.setItemsJson("[]");
        cartRepository.save(cart);
    }

    private Cart createNewCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItemsJson("[]");
        return cartRepository.save(cart);
    }

    private List<CartItem> getCartItems(Cart cart) {
        try {
            return objectMapper.readValue(cart.getItemsJson(), new TypeReference<List<CartItem>>() {});
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        }
    }

    private void updateCartItems(Cart cart, List<CartItem> items) {
        try {
            cart.setItemsJson(objectMapper.writeValueAsString(items));
            
            // Calculate totals
            double totalPrice = items.stream().mapToDouble(CartItem::getSubtotal).sum();
            int totalItems = items.stream().mapToInt(CartItem::getQuantity).sum();
            
            cart.setTotalPrice(totalPrice);
            cart.setTotalItems(totalItems);
            cart.setUpdatedAt(LocalDateTime.now());
        } catch (JsonProcessingException e) {
            // Handle exception
        }
    }

    private CartDto convertToDto(Cart cart) {
        CartDto cartDto = new CartDto();
        BeanUtils.copyProperties(cart, cartDto);
        cartDto.setUserId(cart.getUser().getId());
        cartDto.setItems(getCartItems(cart));
        cartDto.setTotalAmount(cart.getTotalPrice());
        return cartDto;
    }
}
