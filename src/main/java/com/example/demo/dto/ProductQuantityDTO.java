package com.example.demo.dto;

import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link com.example.demo.models.ProductQuantity}
 */

@Value
@Builder
public class ProductQuantityDTO {
    Long id;
    Long productId;
    Long colorId;
    Long sizeId;
    int quantity;
}
