package com.example.demo.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.example.demo.models.Product}
 */

@Value
@Builder
public class ProductDTO {
    Long id;
    String name;
    Double price;
    Long categoryId;
    String description;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<ProductImageDTO> images;
}
