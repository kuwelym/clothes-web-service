package com.example.demo.services.mappers;

import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductImageDTO;
import com.example.demo.models.Category;
import com.example.demo.models.Product;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProductServiceMapper {
    public static ProductDTO toProductDTO(Product product) {
        List<ProductImageDTO> productImageDTOs = Collections.emptyList();
        if (product.getProductImages() != null) {
            productImageDTOs = product.getProductImages().stream()
                    .map(ProductImageServiceMapper::toProductImageDTO)
                    .toList();
        }
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .categoryId(product.getCategory().getId())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .images(productImageDTOs)
                .build();
    }

    public static Product toProduct(ProductDTO productDTO) {
        return Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .category(Category.builder().id(productDTO.getCategoryId()).build())
                .createdAt(productDTO.getCreatedAt())
                .updatedAt(productDTO.getUpdatedAt())
                .productImages(productDTO.getImages().stream().map(ProductImageServiceMapper::toProductImage).collect(Collectors.toList()))
                .build();
    }
}
