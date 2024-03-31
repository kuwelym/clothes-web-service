package com.example.demo.services;

import com.example.demo.dto.ColorDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ColorService {
    List<ColorDTO> findAllColors();
    ColorDTO findColorById(Long id);
    ColorDTO createColor(Long productId, String name, String hexCode);
    ResponseEntity<?> updateColor(Long id, Long productId, String name, String hexCode);
    ResponseEntity<?> deleteColor(Long id);
    Boolean IsProductIdAndHexCodeExist(Long productId, String hexCode);
    void deleteAllColors();
    ResponseEntity<?>  findColorsByProductId(Long productId);
}
