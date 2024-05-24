package com.example.demo.services.mappers;

import com.example.demo.dto.ColorDTO;
import com.example.demo.models.Color;

public class ColorServiceMapper {
    public static ColorDTO toColorDTO(Color color) {
        return ColorDTO.builder()
                .id(color.getId())
                .name(color.getName())
                .hexCode(color.getHexCode())
                .build();
    }

    public static Color toColor(ColorDTO colorDTO) {
        return Color.builder()
                .id(colorDTO.getId())
                .name(colorDTO.getName())
                .hexCode(colorDTO.getHexCode())
                .build();
    }
}
