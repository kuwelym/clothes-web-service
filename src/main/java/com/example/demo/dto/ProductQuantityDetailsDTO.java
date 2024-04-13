package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductQuantityDetailsDTO {
    private Long id;
    private Long productId;
    private ColorDTO color;
    private SizeDTO size;
    private int quantity;
}
