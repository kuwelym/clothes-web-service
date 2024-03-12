package com.example.demo.controller;

import com.example.demo.dto.ProductImageDTO;
import com.example.demo.services.ProductImageService;
import com.example.demo.util.AuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProductImageController {
    private final ProductImageService productImageService;
    @Autowired
    private AuthorizationUtil authorizationUtil;

    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @GetMapping("/public/product-images")
    public List<ProductImageDTO> findAllProductImages() {
        return productImageService.findAllProductImages();
    }

    @GetMapping("/public/product-images/{id}")
    public ResponseEntity<?> findProductImageById(@PathVariable Long id) {
        ProductImageDTO productImageDTO = productImageService.findProductImageById(id);
        if (productImageDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product image not found");
        }
        return ResponseEntity.ok(productImageDTO);
    }

    @PostMapping("/admin/product-images")
    public ResponseEntity<?> createProductImage(
            @RequestBody ProductImageDTO productImageDTO
            , @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }

        return productImageService.createProductImage(productImageDTO.getProductId(), productImageDTO.getUrl());
    }

    @PatchMapping("/admin/product-images/{id}")
    public ResponseEntity<?> updateProductImage(
            @PathVariable Long id,
            @RequestBody ProductImageDTO productImageDTO,
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }

        return productImageService.updateProductImage(id, productImageDTO.getProductId(), productImageDTO.getUrl());
    }

    @DeleteMapping("/admin/product-images/{id}")
    public ResponseEntity<?> deleteProductImage(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }

        return productImageService.deleteProductImage(id);
    }

    @GetMapping("/admin/product-images")
    public ResponseEntity<?> deleteAllProductImages(@RequestHeader("Authorization") String authorization){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }
        productImageService.deleteAllProductImages();
        return ResponseEntity.ok().build();
    }

}
