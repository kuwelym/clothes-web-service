package com.example.demo.dto;


import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link com.example.demo.models.Color}
 */

@Value
@Builder
public class ColorDTO {
    Long id;
    String name;
    String hexCode;
}
