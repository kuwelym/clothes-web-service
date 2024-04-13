package com.example.demo.dto;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ColorDTO {
    private Long id;
    private String name;
    private String hexCode;
}
