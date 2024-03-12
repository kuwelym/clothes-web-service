package com.example.demo.services.mappers;

import com.example.demo.dto.ProductImageDTO;
import com.example.demo.models.Product;
import com.example.demo.models.ProductImage;

public class ProductImageServiceMapper {
    public static ProductImageDTO toProductImageDTO(ProductImage productImage) {
        return ProductImageDTO.builder()
                .id(productImage.getId())
                .url(productImage.getUrl())
                .productId(productImage.getProduct().getId())
                .build();
    }

    public static ProductImage toProductImage(ProductImageDTO productImageDTO) {
        return ProductImage.builder()
                .id(productImageDTO.getId())
                .url(productImageDTO.getUrl())
                .product(Product.builder().id(productImageDTO.getProductId()).build())
                .build();
    }
}
