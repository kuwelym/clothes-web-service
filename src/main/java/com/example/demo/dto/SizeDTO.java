package com.example.demo.dto;

import com.example.demo.models.Color;
import com.example.demo.models.Product;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SizeDTO {
    private Long id;
    private String size;
    private Long colorId;
}
