package com.example.demo.services.mappers;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.models.Category;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryServiceMapper {

    public static CategoryDTO toCategoryDTO(Category category) {
        List<ProductDTO> products = Collections.emptyList(); // Default to an empty list if products are null
        if (category.getProducts() != null) {
            products = category.getProducts().stream()
                    .map(ProductServiceMapper::toProductDTO)
                    .collect(Collectors.toList());
        }
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .imageUrl(category.getImageUrl())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .products(products)
                .build();
    }

    public static Category toCategory(CategoryDTO categoryDTO) {
        return Category.builder()
                .id(categoryDTO.getId())
                .name(categoryDTO.getName())
                .imageUrl(categoryDTO.getImageUrl())
                .createdAt(categoryDTO.getCreatedAt())
                .updatedAt(categoryDTO.getUpdatedAt())
                .products(categoryDTO.getProducts().stream().map(ProductServiceMapper::toProduct).collect(Collectors.toList()))
                .build();
    }
}
