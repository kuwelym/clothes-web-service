package com.example.demo.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ColorDTO {
    private Long id;
    private String name;
    private String hexCode;
    private Long productId;

    private List<SizeDTO> sizes;
}
