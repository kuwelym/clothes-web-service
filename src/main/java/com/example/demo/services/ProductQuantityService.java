package com.example.demo.services;

import com.example.demo.dto.ProductQuantityDetailsDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductQuantityService {
    ResponseEntity<?> getProductQuantities(Long productId);
    List<ProductQuantityDetailsDTO> findAllProductQuantities();
    ResponseEntity<?> addProductQuantity(Long productId, Long colorId, Long sizeId, int quantity);
    ResponseEntity<?> updateProductQuantity(Long productQuantityId, int quantity);
    ResponseEntity<?> deleteProductQuantity(Long productQuantityId);
    ResponseEntity<?> deleteAllProductQuantities(Long productId);
}
