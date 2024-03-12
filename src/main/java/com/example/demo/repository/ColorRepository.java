package com.example.demo.repository;

import com.example.demo.models.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    List<Color> findColorsByProductId(Long productId);
    Color findColorByProductIdAndHexCode(Long productId, String hexCode);
    Boolean existsByProductIdAndHexCode(Long productId, String hexCode);

}
