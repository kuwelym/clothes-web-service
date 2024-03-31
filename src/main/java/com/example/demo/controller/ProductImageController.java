package com.example.demo.controller;

import com.example.demo.dto.ProductImageDTO;
import com.example.demo.services.ProductImageService;
import com.example.demo.util.AuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/public/product-images/by-id/{id}")
    public ResponseEntity<?> findProductImageById(@PathVariable Long id) {
        if (productImageService.findProductImageById(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product image not found");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(productImageService.getImageResponse(productImageService.findProductImageById(id).getImagePath()));
    }

    @GetMapping("/public/product-images/{filename:.+}")
    public ResponseEntity<?> findProductImageByFilename(@PathVariable String filename) {
        if(productImageService.getImageResponse(filename) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(productImageService.getImageResponse(filename));
    }


    @GetMapping("/public/products/{productId}/product-images")
    public ResponseEntity<?> findProductImagesByProductId(@PathVariable Long productId) {
        List<String> productImageFilename = productImageService.findProductImageUrlsByProductId(productId);

        if (productImageFilename.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product images not found");
        }

        return ResponseEntity.ok().body(productImageFilename);
    }

    @PostMapping(path = "/admin/product-images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createProductImage(
            @ModelAttribute ProductImageDTO productImageDTO,
            @RequestBody MultipartFile imageFile,
            @RequestHeader("Authorization") String authorization
    ) {
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }

        return productImageService.createProductImage(productImageDTO.getProductId(), imageFile);
    }

    @PatchMapping(path = "/admin/product-images/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateProductImage(
            @PathVariable Long id,
            @ModelAttribute ProductImageDTO productImageDTO,
            @RequestBody MultipartFile imageFile,
            @RequestHeader("Authorization") String authorization
    ) {
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }

        return productImageService.updateProductImage(id, productImageDTO.getProductId(), imageFile);
    }

    @DeleteMapping("/admin/product-images/{id}")
    public ResponseEntity<?> deleteProductImage(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization
    ) {
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }

        return productImageService.deleteProductImage(id);
    }

    @GetMapping("/admin/product-images")
    public ResponseEntity<?> deleteAllProductImages(@RequestHeader("Authorization") String authorization) {
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }
        productImageService.deleteAllProductImages();
        return ResponseEntity.ok().build();
    }

}
