package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.example.demo.models.StorePickup}
 */
@Data
@Builder
public class StorePickupDTO implements Serializable {
    Long id;
    @NotNull(message = "Store cannot be null")
    public String store;
    public LocalDate pickupDate;
}