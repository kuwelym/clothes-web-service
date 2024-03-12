package com.example.demo.services;

import com.example.demo.dto.SizeDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface SizeService {
    public ResponseEntity<?> addSize(String size, Long colorId);
    public SizeDTO getSizesById(Long id);
    public List<SizeDTO> getAllSizes();
    public ResponseEntity<?> deleteSize(Long id);
    public ResponseEntity<?> updateSize(Long id, String size);
}
