package com.example.demo.repository;

import com.example.demo.models.Color;
import com.example.demo.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByNameAndCategory(String name, String category);
    Boolean existsByNameAndCategory(String name, String category);
}
