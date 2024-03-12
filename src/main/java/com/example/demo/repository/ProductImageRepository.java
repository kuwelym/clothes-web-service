package com.example.demo.repository;

import com.example.demo.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long>{
    Boolean existsByProductIdAndUrl(Long productId, String url);
}
