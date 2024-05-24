package com.example.demo.services.impl;

import com.example.demo.dto.ProductDTO;
import com.example.demo.models.Category;
import com.example.demo.models.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.services.ProductService;
import com.example.demo.services.mappers.ProductServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
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
    public ProductDTO createProduct(String name, Double price, Long categoryId, String description) {
        Category categoryEntity = categoryRepository.findById(categoryId).orElse(null);
        if(productRepository.existsByNameAndCategoryId(name, categoryId)){
            return null;
        }
        Product product = Product.builder()
                .name(name)
                .price(price)
                .category(categoryEntity)
                .description(description)
                .build();
        productRepository.save(product);
        return ProductServiceMapper.toProductDTO(product);
    }

    @Override
    public ProductDTO updateProduct(Long id, String name, Double price, Long categoryId, String description) {
        Category categoryEntity = categoryRepository.findById(categoryId).orElse(null);
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setName(name);
            product.setPrice(price);
            product.setCategory(categoryEntity);
            productRepository.save(product);
            return ProductServiceMapper.toProductDTO(product);
        }
        return null;
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
