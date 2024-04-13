package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private Double price;
    private Long categoryId;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProductImageDTO> images;
}
