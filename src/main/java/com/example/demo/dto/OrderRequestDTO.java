package com.example.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * DTO for order request
 */
@Value
@Builder
public class OrderRequestDTO {
    @NotEmpty(message = "Order items cannot be empty")
    List<OrderItemDTO> items;
    String address;
    @NotNull(message = "Classification cannot be null")
    String classification;
    @NotNull(message = "Delivery option cannot be null")
    String deliveryOption;
    String pickupPlace;
}
