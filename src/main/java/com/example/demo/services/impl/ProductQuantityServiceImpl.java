package com.example.demo.services.impl;

import com.example.demo.dto.ProductQuantityDetailsDTO;
import com.example.demo.models.Color;
import com.example.demo.models.Product;
import com.example.demo.models.ProductQuantity;
import com.example.demo.models.Size;
import com.example.demo.repository.ColorRepository;
import com.example.demo.repository.ProductQuantityRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SizeRepository;
import com.example.demo.services.ProductQuantityService;
import com.example.demo.services.mappers.ProductQuantityServiceMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductQuantityServiceImpl implements ProductQuantityService {
    private final ProductQuantityRepository productQuantityRepository;
    private final ProductRepository productRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;

    public ProductQuantityServiceImpl(ProductQuantityRepository productQuantityRepository, ProductRepository productRepository, ColorRepository colorRepository, SizeRepository sizeRepository) {
        this.productQuantityRepository = productQuantityRepository;
        this.productRepository = productRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
    }


    @Override
    public ResponseEntity<?> getProductQuantities(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if(product == null){
            return ResponseEntity.badRequest().body("Product not found");
        }
        List<ProductQuantity> productQuantities = productQuantityRepository.findAllByProductId(productId);
        return ResponseEntity.ok().body(productQuantities
                .stream()
                .map(ProductQuantityServiceMapper::toProductQuantityDetailsDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public List<ProductQuantityDetailsDTO> findAllProductQuantities() {
        return productQuantityRepository.findAll()
                .stream()
                .map(ProductQuantityServiceMapper::toProductQuantityDetailsDTO)
                .collect(Collectors.toList());

    }

    @Override
    public ResponseEntity<?> findProductQuantity(Long productId, Long colorId, Long sizeId) {
        ProductQuantity productQuantity = productQuantityRepository.findByProductIdAndColorIdAndSizeId(productId, colorId, sizeId);
        if(productQuantity == null){
            return ResponseEntity.badRequest().body("Product quantity not found");
        }
        return ResponseEntity.ok().body(ProductQuantityServiceMapper.toProductQuantityDetailsDTO(productQuantity));
    }

    @Override
    public ResponseEntity<?> addProductQuantity(Long productId, Long colorId, Long sizeId, int quantity) {
        if(productQuantityRepository.existsByProductIdAndColorIdAndSizeId(productId, colorId, sizeId)){
            return ResponseEntity.badRequest().body("Product quantity already exists");
        }
        Product product = productRepository.findById(productId).orElse(null);
        if(product == null){
            return ResponseEntity.badRequest().body("Product not found");
        }
        Color color = colorRepository.findById(colorId).orElse(null);
        if(color == null){
            return ResponseEntity.badRequest().body("Color not found");
        }
        Size size = sizeRepository.findById(sizeId).orElse(null);
        if(size == null){
            return ResponseEntity.badRequest().body("Size not found");
        }
        ProductQuantity productQuantity = ProductQuantity.builder()
                .product(product)
                .color(color)
                .size(size)
                .quantity(quantity)
                .build();
        productQuantityRepository.save(productQuantity);
        return ResponseEntity.ok().body("Product quantity added");
    }

    @Override
    public ResponseEntity<?> updateProductQuantity(Long productQuantityId, int quantity) {
        ProductQuantity productQuantity = productQuantityRepository.findById(productQuantityId).orElse(null);
        if(productQuantity == null){
            return ResponseEntity.badRequest().body("Product quantity not found");
        }
        productQuantity.setQuantity(quantity);
        productQuantityRepository.save(productQuantity);
        return ResponseEntity.ok().body("Product quantity updated");
    }

    @Override
    public ResponseEntity<?> deleteProductQuantity(Long productQuantityId) {
        ProductQuantity productQuantity = productQuantityRepository.findById(productQuantityId).orElse(null);
        if(productQuantity == null){
            return ResponseEntity.badRequest().body("Product quantity not found");
        }
        productQuantityRepository.delete(productQuantity);
        return ResponseEntity.ok().body("Product quantity deleted");
    }

    @Override
    public ResponseEntity<?> deleteAllProductQuantities(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if(product == null){
            return ResponseEntity.badRequest().body("Product not found");
        }
        productQuantityRepository.deleteAllByProductId(productId);

        return ResponseEntity.ok().body("Product quantities deleted");
    }
}
