package com.example.demo.services.impl;

import com.example.demo.models.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.services.CategoryService;
import com.example.demo.services.mappers.CategoryServiceMapper;
import com.example.demo.services.mappers.ProductServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ResponseEntity<?> deleteAllCategories() {
        try {
            categoryRepository.deleteAll();
            return ResponseEntity.ok().body("All categories have been deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @Override
    public ResponseEntity<?> createCategory(String name, String imageUrl) {
        if(categoryRepository.existsByName(name)){
            return ResponseEntity.badRequest().body("Error: Category with name " + name + " already exists");
        }
        try {
            Category category = Category
                    .builder()
                    .name(name)
                    .imageUrl(imageUrl)
                    .build();
            categoryRepository.save(category);
            return ResponseEntity.ok().body(category);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @Override
    public ResponseEntity<?> deleteCategory(Long id) {
        try {
            categoryRepository.deleteById(id);
            return ResponseEntity.ok().body("Category with id " + id + " has been deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @Override
    public ResponseEntity<?> updateCategory(Long id, String name, String imageUrl) {
        try {
            Category category = categoryRepository.findCategoryById(id);
            category.setName(name);
            category.setImageUrl(imageUrl);
            categoryRepository.save(category);
            return ResponseEntity.ok().body("Category with id " + id + " has been updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @Override
    public ResponseEntity<?> findAllCategories() {
        try {
            return ResponseEntity.ok().body(
                    categoryRepository.findAll()
                            .stream()
                            .map(CategoryServiceMapper::toCategoryDTO)
                            .collect(Collectors.toList())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @Override
    public ResponseEntity<?> findCategoryById(Long id) {
        try {
            return ResponseEntity.ok().body(categoryRepository.findCategoryById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @Override
    public ResponseEntity<?> findCategoryByName(String name) {
        try {
            return ResponseEntity.ok().body(categoryRepository.findByName(name));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }

    @Override
    public ResponseEntity<?> findProductsByCategoryId(Long id, int offset, int size) {
        try {
            return ResponseEntity.ok().body(
                    categoryRepository.findCategoryById(id)
                            .getProducts()
                            .stream()
                            .skip(offset)
                            .limit(size)
                            .map(ProductServiceMapper::toProductDTO)
                            .collect(Collectors.toList())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e);
        }
    }
}
