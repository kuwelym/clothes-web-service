package com.example.demo.services.impl;

import com.example.demo.dto.ProductImageDTO;
import com.example.demo.models.Product;
import com.example.demo.models.ProductImage;
import com.example.demo.repository.ProductImageRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.services.ProductImageService;
import com.example.demo.services.mappers.ProductImageServiceMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageRepository productImageRepository;

    public ProductImageServiceImpl(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }


    @Override
    public List<ProductImageDTO> findAllProductImages() {
        List<ProductImage> productImageDTOS = productImageRepository.findAll();
        return productImageDTOS.stream()
                .map(ProductImageServiceMapper::toProductImageDTO)
                .collect(Collectors.toList());

    }

    @Override
    public ProductImageDTO findProductImageById(Long id) {
        return productImageRepository.findById(id)
                .map(ProductImageServiceMapper::toProductImageDTO)
                .orElse(null);
    }

    @Override
    public ResponseEntity<?> createProductImage(Long productId, String url) {
        if (productImageRepository.existsByProductIdAndUrl(productId, url)) {
            return ResponseEntity.badRequest().body("Product id and url already exist");
        }
        ProductImage productImage = ProductImage.builder()
                .url(url)
                .product(Product.builder().id(productId).build())
                .build();
        productImageRepository.save(productImage);
        return ResponseEntity.ok(ProductImageServiceMapper.toProductImageDTO(productImage));
    }

    @Override
    public ResponseEntity<?> updateProductImage(Long id, Long productId, String url) {
        ProductImage productImage = productImageRepository.findById(id).orElse(null);
        if (productImage == null) {
            return ResponseEntity.badRequest().body("Product image not found");
        }
        if (productImageRepository.existsByProductIdAndUrl(productId, url)) {
            return ResponseEntity.badRequest().body("Product id and url already exist");
        }
        productImage.setUrl(url);
        productImage.setProduct(Product.builder().id(productId).build());
        productImageRepository.save(productImage);
        return ResponseEntity.ok(ProductImageServiceMapper.toProductImageDTO(productImage));
    }

    @Override
    public ResponseEntity<?> deleteProductImage(Long id) {
        ProductImage productImage = productImageRepository.findById(id).orElse(null);
        if (productImage == null) {
            return ResponseEntity.badRequest().body("Product image not found");
        }
        productImageRepository.delete(productImage);
        return ResponseEntity.ok("Product image deleted");
    }

    @Override
    public void deleteAllProductImages() {
        productImageRepository.deleteAll();
    }

}
