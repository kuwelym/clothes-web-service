package com.example.demo.services.mappers;

import com.example.demo.dto.ColorDTO;
import com.example.demo.dto.SizeDTO;
import com.example.demo.models.Color;
import com.example.demo.models.Product;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ColorServiceMapper {
    public static ColorDTO toColorDTO(Color color) {
        List<SizeDTO> sizeDTOs = Collections.emptyList(); // Default to an empty list if sizes are null
        if (color.getSizes() != null) {
            sizeDTOs = color.getSizes().stream()
                    .map(SizeServiceMapper::toSizeDTO)
                    .collect(Collectors.toList());
        }
        return ColorDTO.builder()
                .id(color.getId())
                .name(color.getName())
                .hexCode(color.getHexCode())
                .productId(color.getProduct().getId())
                .sizes(sizeDTOs)
                .build();
    }

    public static Color toColor(ColorDTO colorDTO) {
        return Color.builder()
                .id(colorDTO.getId())
                .name(colorDTO.getName())
                .hexCode(colorDTO.getHexCode())
                .product(Product.builder().id(colorDTO.getProductId()).build())
                .sizes(colorDTO.getSizes().stream().map(SizeServiceMapper::toSize).collect(Collectors.toList()))
                .build();
    }
}
