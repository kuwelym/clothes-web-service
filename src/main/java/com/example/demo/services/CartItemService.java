package com.example.demo.services;

import org.springframework.http.ResponseEntity;

public interface CartItemService {
    ResponseEntity<?> getCartItems(Long userId);
    ResponseEntity<?> addCartItem(Long userId, Long productId, int quantity, Long colorId, Long sizeId);
    ResponseEntity<?> updateCartItem(Long userId, Long cartItemId, int quantity);
    ResponseEntity<?> deleteCartItem(Long userId, Long cartItemId);
    ResponseEntity<?> deleteAllCartItems(Long userId);
}
