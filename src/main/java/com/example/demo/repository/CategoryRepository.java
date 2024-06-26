package com.example.demo.repository;

import com.example.demo.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
    Category findByName(String name);
    Boolean existsByName(String name);
    Category findCategoryById(Long id);
}
