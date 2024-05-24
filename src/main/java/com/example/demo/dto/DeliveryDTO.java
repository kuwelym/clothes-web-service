package com.example.demo.dto;

import com.example.demo.models.Delivery;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Delivery}
 */
@Value
@Builder
public class DeliveryDTO implements Serializable {
    Long id;
    @NotNull(message = "Delivery date cannot be null")
    LocalDate deliveryDate;
    @NotNull(message = "Address cannot be null")
    String address;
}