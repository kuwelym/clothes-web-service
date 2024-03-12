package com.example.demo.services.mappers;

import com.example.demo.dto.SizeDTO;
import com.example.demo.models.Color;
import com.example.demo.models.Size;

public class SizeServiceMapper {
    public static SizeDTO toSizeDTO(Size size) {
        return SizeDTO.builder()
                .id(size.getId())
                .size(size.getSize())
                .colorId(size.getColor().getId())
                .build();
    }

    public static Size toSize(SizeDTO sizeDTO) {
        return Size.builder()
                .id(sizeDTO.getId())
                .size(sizeDTO.getSize())
                .color(Color.builder().id(sizeDTO.getColorId()).build())
                .build();
    }
}
