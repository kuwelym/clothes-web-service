package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemDTO {
    private Long id;
    private Long productId;
    private int quantity;
    private double totalPrice;
    private Long selectedColorId;
    private Long selectedSizeId;
    private Long userId;
}
