package com.example.demo.dto;

import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link com.example.demo.models.ProductImage}
 */

@Value
@Builder
public class ProductQuantityDetailsDTO {
    Long id;
    Long productId;
    ColorDTO color;
    SizeDTO size;
    int quantity;
}
