package com.example.demo.services.mappers;

import com.example.demo.dto.ProductQuantityDTO;
import com.example.demo.dto.ProductQuantityDetailsDTO;
import com.example.demo.models.ProductQuantity;

public class ProductQuantityServiceMapper {
    public static ProductQuantityDTO toProductQuantityDTO(ProductQuantity productQuantity) {
        return ProductQuantityDTO.builder()
                .id(productQuantity.getId())
                .productId(productQuantity.getProduct().getId())
                .colorId(productQuantity.getColor().getId())
                .sizeId(productQuantity.getSize().getId())
                .quantity(productQuantity.getQuantity())
                .build();
    }

    public static ProductQuantityDetailsDTO toProductQuantityDetailsDTO(ProductQuantity productQuantity) {
        return ProductQuantityDetailsDTO.builder()
                .id(productQuantity.getId())
                .productId(productQuantity.getProduct().getId())
                .color(ColorServiceMapper.toColorDTO(productQuantity.getColor()))
                .size(SizeServiceMapper.toSizeDTO(productQuantity.getSize()))
                .quantity(productQuantity.getQuantity())
                .build();
    }
}
