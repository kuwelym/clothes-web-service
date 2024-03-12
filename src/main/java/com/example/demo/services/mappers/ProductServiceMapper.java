package com.example.demo.services.mappers;

import com.example.demo.dto.ColorDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductImageDTO;
import com.example.demo.models.Product;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProductServiceMapper {
    public static ProductDTO toProductDTO(Product product) {
        List<ColorDTO> colorDTOs = Collections.emptyList();
        if (product.getColors() != null) {
            colorDTOs = product.getColors().stream()
                    .map(ColorServiceMapper::toColorDTO)
                    .collect(Collectors.toList());
        }
        List<ProductImageDTO> productImageDTOs = Collections.emptyList();
        if (product.getProductImages() != null) {
            productImageDTOs = product.getProductImages().stream()
                    .map(ProductImageServiceMapper::toProductImageDTO)
                    .collect(Collectors.toList());
        }
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .category(product.getCategory())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .colors(colorDTOs)
                .images(productImageDTOs)
                .build();
    }

    public static Product toProduct(ProductDTO productDTO) {
        return Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .category(productDTO.getCategory())
                .createdAt(productDTO.getCreatedAt())
                .updatedAt(productDTO.getUpdatedAt())
                .colors(productDTO.getColors().stream().map(ColorServiceMapper::toColor).collect(Collectors.toList()))
                .productImages(productDTO.getImages().stream().map(ProductImageServiceMapper::toProductImage).collect(Collectors.toList()))
                .build();
    }
}
