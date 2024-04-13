package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductQuantityDTO {
    private Long id;
    private Long productId;
    private Long colorId;
    private Long sizeId;
    private int quantity;
}
