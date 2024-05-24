package com.example.demo.services;

import com.example.demo.dto.ProductDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    List<ProductDTO> findAllProducts();
    ProductDTO findProductById(Long id);
    ProductDTO createProduct(String name, Double price, Long categoryId, String description);
    ProductDTO updateProduct(Long id, String name, Double price, Long categoryId, String description);
    ResponseEntity<?> deleteProduct(Long id);
    List<ProductDTO> findProductsPaginated(int offset, int size);
}
