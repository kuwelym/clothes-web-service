package com.example.demo.services;

import com.example.demo.dto.ProductDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    List<ProductDTO> findAllProducts();
    ProductDTO findProductById(Long id);
    ProductDTO createProduct(String name, Double price, Long categoryId, String description, Integer availableQuantity);
    ProductDTO updateProduct(Long id, String name, Double price, Long categoryId, String description, Integer availableQuantity);
    ResponseEntity<?> deleteProduct(Long id);}
