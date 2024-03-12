package com.example.demo.controller;

import com.example.demo.dto.ColorDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.services.ProductService;
import com.example.demo.util.AuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1")
@RestController
public class ProductController {
    private final ProductService productService;
    @Autowired
    private AuthorizationUtil authorizationUtil;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/public/products")
    public List<ProductDTO> getProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/public/products/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        Optional<ProductDTO> product = Optional.ofNullable(productService.findProductById(id));
        return product.orElse(null);
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
    @PostMapping("/admin/products")
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

        product = productService.createProduct(productDTO.getName(), productDTO.getPrice(), productDTO.getCategory());
        if (product == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Product already exists");
        }
        return ResponseEntity.ok(product);
    }

//    @PostMapping("/admin/products")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO, @RequestHeader("Authorization") String token){
//        ProductDTO product;
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//            if (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ADMIN"))) {
//                // ... create product logic (user has ADMIN role)
//                product = productService.createProduct(productDTO.getName(), productDTO.getPrice(), productDTO.getCategory());
//                if (product == null) {
//                    return ResponseEntity.status(HttpStatus.CONFLICT)
//                            .body("Product already exists");
//                }
//            }
//            else {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                        .body("User is not authorized to create a product");
//
//            }
//        }
//        else{
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("User is not authenticated");
//        }
//        return ResponseEntity.ok(product);
//    }

    @PatchMapping("/admin/products/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDTO productDTO,
            @RequestHeader("Authorization") String authorization
    ) {
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }

        ProductDTO updatedProduct = productService.updateProduct(id, productDTO.getName(), productDTO.getPrice(), productDTO.getCategory());
        if (updatedProduct == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found");
        }
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/public/products/{productId}/colors")
    public ResponseEntity<?> getColorsByProductId(@PathVariable Long productId) {
        List<ColorDTO> colors = productService.findColorsByProductId(productId);
        if (colors == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found");
        }
        return ResponseEntity.ok(colors);
    }

    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, @RequestHeader("Authorization") String authorization){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }
        return productService.deleteProduct(id);
    }
}
