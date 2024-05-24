package com.example.demo.controller;

import com.example.demo.dto.ProductQuantityDTO;
import com.example.demo.dto.ProductQuantityDetailsDTO;
import com.example.demo.services.ProductQuantityService;
import com.example.demo.util.AuthorizationUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class ProductQuantityController {
    private final ProductQuantityService productQuantityService;
    private final AuthorizationUtil authorizationUtil;

    public ProductQuantityController(ProductQuantityService productQuantityService, AuthorizationUtil authorizationUtil) {
        this.productQuantityService = productQuantityService;
        this.authorizationUtil = authorizationUtil;
    }

    @GetMapping("/product-quantities/{productId}")
    public ResponseEntity<?> findAllProductQuantities(@PathVariable Long productId) {
        return productQuantityService.getProductQuantities(productId);
    }

    @GetMapping("/product-quantities")
    public List<ProductQuantityDetailsDTO> findAllProductQuantities() {
        return productQuantityService.findAllProductQuantities();
    }

    @GetMapping("/product-quantities/{productId}/{colorId}/{sizeId}")
    public ResponseEntity<?> findProductQuantity(
            @PathVariable Long productId,
            @PathVariable Long colorId,
            @PathVariable Long sizeId
    ) {
        return productQuantityService.findProductQuantity(productId, colorId, sizeId);
    }

    @PostMapping("/product-quantities")
    public ResponseEntity<?> addProductQuantity(
            @RequestBody ProductQuantityDTO productQuantity,
            @RequestHeader("Authorization") String token
    ) {
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(token);
        if (response != null) {
            return response;
        }
        return productQuantityService.addProductQuantity(productQuantity.getProductId(), productQuantity.getColorId(), productQuantity.getSizeId(), productQuantity.getQuantity());
    }

    @PatchMapping("/product-quantities/{productQuantityId}")
    public ResponseEntity<?> updateProductQuantity(
            @PathVariable Long productQuantityId,
            @RequestBody ProductQuantityDTO productQuantity,
            @RequestHeader("Authorization") String token
    ) {
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(token);
        if (response != null) {
            return response;
        }
        return productQuantityService.updateProductQuantity(productQuantityId, productQuantity.getQuantity());
    }

    @DeleteMapping("/product-quantities/{productQuantityId}")
    public ResponseEntity<?> deleteProductQuantity(
            @PathVariable Long productQuantityId,
            @RequestHeader("Authorization") String token
    ) {
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(token);
        if (response != null) {
            return response;
        }
        return productQuantityService.deleteProductQuantity(productQuantityId);
    }

    @DeleteMapping("/product-quantities")
    public ResponseEntity<?> deleteAllProductQuantities(
            @RequestParam Long productId,
            @RequestHeader("Authorization") String token
    ) {
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(token);
        if (response != null) {
            return response;
        }
        return productQuantityService.deleteAllProductQuantities(productId);
    }
}
