package com.example.demo.dto;

import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link com.example.demo.models.CartItem}
 */

@Value
@Builder
public class CartItemDTO {
    Long id;
    Long productId;
    int quantity;
    double totalPrice;
    Long selectedColorId;
    Long selectedSizeId;
    Long userId;
}
