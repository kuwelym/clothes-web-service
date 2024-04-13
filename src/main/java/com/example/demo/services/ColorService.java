package com.example.demo.services;

import com.example.demo.dto.ColorDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ColorService {
    List<ColorDTO> findAllColors();
    ColorDTO createColor(String hexCode);
    ResponseEntity<?> updateColor(Long id, String hexCode);
    ResponseEntity<?> deleteColor(Long id);
    void deleteAllColors();
}
