package com.example.demo.dto;

import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link com.example.demo.models.ProductImage}
 */

@Builder
@Value
public class ProductImageDTO {
    Long id;
    String imagePath;
    Long productId;
}
