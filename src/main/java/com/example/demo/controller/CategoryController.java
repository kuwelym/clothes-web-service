package com.example.demo.controller;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.services.CategoryService;
import com.example.demo.util.AuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    private AuthorizationUtil authorizationUtil;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<?> createCategory(
            @RequestBody CategoryDTO categoryDTO,
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }
        return categoryService.createCategory(categoryDTO.getName(), categoryDTO.getImageUrl());
    }

    @GetMapping("/public/categories")
    public ResponseEntity<?> getCategories() {
        return categoryService.findAllCategories();
    }

    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }
        return categoryService.deleteCategory(id);
    }

    @DeleteMapping("/admin/categories")
    public ResponseEntity<?> deleteAllCategories(
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }
        return categoryService.deleteAllCategories();
    }

    @PatchMapping("/admin/categories/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDTO categoryDTO,
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }
        return categoryService.updateCategory(id, categoryDTO.getName(), categoryDTO.getImageUrl());
    }

    @GetMapping("/public/categories/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        return categoryService.findCategoryById(id);
    }

    @GetMapping("/public/categories/{name}")
    public ResponseEntity<?> getCategoryByName(@PathVariable String name) {
        return categoryService.findCategoryByName(name);
    }

    @GetMapping("/public/categories/{id}/products")
    public ResponseEntity<?> getProductsByCategoryId(@PathVariable Long id) {
        return categoryService.findProductsByCategoryId(id);
    }

}
