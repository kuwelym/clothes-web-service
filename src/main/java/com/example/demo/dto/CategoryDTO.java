package com.example.demo.dto;

import lombok.Builder;
import lombok.Value;


import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.example.demo.models.Category}
 */

@Value
@Builder
public class CategoryDTO {
    Long id;
    String name;
    String imageUrl;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    List<ProductDTO> products;

}
