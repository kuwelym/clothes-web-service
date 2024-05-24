package com.example.demo.services.mappers;

import com.example.demo.dto.SizeDTO;
import com.example.demo.models.Size;

public class SizeServiceMapper {
    public static SizeDTO toSizeDTO(Size size) {
        return SizeDTO.builder()
                .id(size.getId())
                .size(size.getSize())
                .build();
    }

    public static Size toSize(SizeDTO sizeDTO) {
        return Size.builder()
                .id(sizeDTO.getId())
                .size(sizeDTO.getSize())
                .build();
    }
}
