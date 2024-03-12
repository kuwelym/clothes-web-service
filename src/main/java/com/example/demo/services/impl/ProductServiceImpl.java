package com.example.demo.services.impl;

import com.example.demo.dto.ColorDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.models.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.services.ProductService;
import com.example.demo.services.mappers.ProductServiceMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
        initializeProducts();
    }

    @Override
    public void initializeProducts() {
        if (productRepository.count() == 0) {
            createProduct("Product 1", 100.0, "Category 1");
            createProduct("Product 2", 200.0, "Category 2");
            createProduct("Product 3", 300.0, "Category 3");
        }
    }

    @Override
    public List<ProductDTO> findAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map((ProductServiceMapper::toProductDTO)).collect(Collectors.toList());
    }

    @Override
    public ProductDTO findProductById(Long id) {
        return productRepository.findById(id).map(ProductServiceMapper::toProductDTO).orElse(null);
    }

    @Override
    public ProductDTO createProduct(String name, Double price, String category) {
        if(productRepository.existsByNameAndCategory(name, category)){
            return null;
        }
        Product product = Product.builder()
                .name(name)
                .price(price)
                .category(category)
                .build();
        productRepository.save(product);
        return ProductServiceMapper.toProductDTO(product);
    }

    @Override
    public ProductDTO updateProduct(Long id, String name, Double price, String category) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setName(name);
            product.setPrice(price);
            product.setCategory(category);
            productRepository.save(product);
            return ProductServiceMapper.toProductDTO(product);
        }
        return null;
    }

    @Override
    public List<ColorDTO> findColorsByProductId(Long productId) {
        Product product = productRepository
                .findById(productId)
                .orElse(null);
        if (product != null) {
            return product.getColors().stream().map(color -> ColorDTO.builder()
                    .id(color.getId())
                    .name(color.getName())
                    .hexCode(color.getHexCode())
                    .productId(color.getProduct().getId())
                    .build()).collect(Collectors.toList());
        } else {
            return null;
        }
    }

    @Override
    public ResponseEntity<?> deleteProduct(Long id) {
        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(404).body("Product not found");
    }

}
