package com.example.demo.services;

import com.example.demo.dto.ProductImageDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductImageService {
    List<ProductImageDTO> findAllProductImages();
    ProductImageDTO findProductImageById(Long id);
    ResponseEntity<?> createProductImage(Long productId, String url);
    ResponseEntity<?> updateProductImage(Long id, Long productId, String url);
    ResponseEntity<?> deleteProductImage(Long id);
    void deleteAllProductImages();
    List<ProductImageDTO> findProductImagesByProductId(Long productId);
}
