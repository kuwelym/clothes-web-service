package com.example.demo.dto;

import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link com.example.demo.models.Size}
 */

@Builder
@Value
public class SizeDTO {
    Long id;
    String size;
}
