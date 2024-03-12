package com.example.demo.dto;

import com.example.demo.models.Color;
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
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<ColorDTO> colors;
    private List<ProductImageDTO> images;
}
