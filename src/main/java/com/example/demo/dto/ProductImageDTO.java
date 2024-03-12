package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductImageDTO {
    private Long id;
    private String url;
    private Long productId;
}
