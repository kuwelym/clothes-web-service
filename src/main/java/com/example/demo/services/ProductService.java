package com.example.demo.services;

import com.example.demo.dto.ColorDTO;
import com.example.demo.dto.ProductDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    void initializeProducts();
    List<ProductDTO> findAllProducts();
    ProductDTO findProductById(Long id);
    ProductDTO createProduct(String name, Double price, String category);
    ProductDTO updateProduct(Long id, String name, Double price, String category);
    List<ColorDTO> findColorsByProductId(Long productId);
    ResponseEntity<?> deleteProduct(Long id);}
