package com.example.demo.services;


import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<?> deleteAllCategories();
    ResponseEntity<?> createCategory(String name, String imageUrl);
    ResponseEntity<?> deleteCategory(Long id);
    ResponseEntity<?> updateCategory(Long id, String name, String imageUrl);
    ResponseEntity<?> findAllCategories();
    ResponseEntity<?> findCategoryById(Long id);
    ResponseEntity<?> findCategoryByName(String name);
    ResponseEntity<?> findProductsByCategoryId(Long id, int offset, int size);
}
