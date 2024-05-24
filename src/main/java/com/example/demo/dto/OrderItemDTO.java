package com.example.demo.dto;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.demo.models.OrderItem}
 */
@Value
@Builder
public class OrderItemDTO implements Serializable {
    Long id;
    Long productId;
    Long selectedColorId;
    Long selectedSizeId;
    int quantity;
    double totalPrice;
}