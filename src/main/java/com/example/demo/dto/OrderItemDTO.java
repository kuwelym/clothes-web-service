package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.example.demo.models.OrderItem}
 */
@Builder
@Data
public class OrderItemDTO implements Serializable {
    Long id;
    Long productId;
    Long colorId;
    Long sizeId;
    int quantity;
    double totalPrice;
}