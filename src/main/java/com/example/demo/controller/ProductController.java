package com.example.demo.controller;

import com.example.demo.dto.ProductDTO;
import com.example.demo.services.ProductService;
import com.example.demo.util.AuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class ProductController {
    private final ProductService productService;
    private final AuthorizationUtil authorizationUtil;

    @Autowired
    public ProductController(ProductService productService, AuthorizationUtil authorizationUtil) {
        this.productService = productService;
        this.authorizationUtil = authorizationUtil;
    }

    @GetMapping("/products")
    public ResponseEntity<?> getProducts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {
        // Calculate the offset based on the page and size
        int offset = page * size;

        // Fetch products based on pagination parameters
        List<ProductDTO> products = productService.findProductsPaginated(offset, size);
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No products found");
        }

        String eTag = calculateETagForProducts(products);
        if (ifNoneMatch != null && ifNoneMatch.equals(eTag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .eTag(eTag)
                    .body(null);
        }
        return ResponseEntity.ok()
                .eTag(eTag)
                .body(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id, @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {
        ProductDTO product = productService.findProductById(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found");
        }

        String eTag = calculateETagForProduct(product);
        if (ifNoneMatch != null && ifNoneMatch.equals(eTag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .eTag(eTag)
                    .body(null);
        }
        return ResponseEntity.ok()
                .eTag(eTag)
                .body(product);
    }
    /**
     * Create a new product
     * @param productDTO - product details
     * @param authorization - Bearer token
     *                      - required: true
     *                      - in: header
     * @return - created product
     *
     */
    @PostMapping("/products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(
            @RequestBody ProductDTO productDTO,
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }

        ProductDTO product;

        product = productService.createProduct(productDTO.getName(), productDTO.getPrice(), productDTO.getCategoryId(), productDTO.getDescription());
        if (product == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Product already exists");
        }
        return ResponseEntity.ok(product);
    }

    @PatchMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDTO productDTO,
            @RequestHeader("Authorization") String authorization
    ) {
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }

        ProductDTO updatedProduct = productService.updateProduct(id, productDTO.getName(), productDTO.getPrice(), productDTO.getCategoryId(), productDTO.getDescription());
        if (updatedProduct == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found");
        }
        return ResponseEntity.ok(updatedProduct);
    }


    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, @RequestHeader("Authorization") String authorization){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }
        return productService.deleteProduct(id);
    }

    private String calculateETagForProducts(List<ProductDTO> products) {
        StringBuilder content = new StringBuilder();
        for (ProductDTO product : products) {
            content.append(product.toString());
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content.toString().getBytes());
            return "\"" + Base64.getEncoder().encodeToString(hash) + "\"";
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String calculateETagForProduct(ProductDTO product) {
        String content = product.toString(); // Generate a string representation of the product
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content.getBytes());
            return "\"" + Base64.getEncoder().encodeToString(hash) + "\"";
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
