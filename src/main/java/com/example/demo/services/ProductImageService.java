package com.example.demo.services;

import com.example.demo.dto.ProductImageDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {
    List<ProductImageDTO> findAllProductImages();
    ProductImageDTO findProductImageById(Long id);
    ResponseEntity<?> createProductImage(Long productId, MultipartFile imageFile);
    ResponseEntity<?> updateProductImage(Long id, Long productId, MultipartFile imageFile);
    ResponseEntity<?> deleteProductImage(Long id);
    Resource getImageResponse(String imagePath);
    void deleteAllProductImages();
    List<String> findProductImageUrlsByProductId(Long productId);
}
