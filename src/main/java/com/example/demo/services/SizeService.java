package com.example.demo.services;

import com.example.demo.dto.SizeDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface SizeService {
    ResponseEntity<?> addSize(String size, Long colorId);
    SizeDTO getSizesById(Long id);
    List<SizeDTO> getAllSizes();
    ResponseEntity<?> deleteSize(Long id);
    ResponseEntity<?> updateSize(Long id, String size);
    List<SizeDTO> getSizesByColorId(Long colorId);
}
