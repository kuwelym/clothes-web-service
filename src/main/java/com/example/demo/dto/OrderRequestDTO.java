package com.example.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.util.List;

@Value
@Builder
@Data
public class OrderRequestDTO {
    @NotEmpty(message = "Order items cannot be empty")
    List<OrderItemDTO> items;
    @NotNull(message = "Address cannot be null")
    String address;
    @NotNull(message = "Classification cannot be null")
    String classification;
    @NotNull(message = "Delivery option cannot be null")
    String deliveryOption;
}
