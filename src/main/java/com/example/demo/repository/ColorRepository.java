package com.example.demo.repository;

import com.example.demo.models.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    boolean existsByHexCode(String hexCode);

}
