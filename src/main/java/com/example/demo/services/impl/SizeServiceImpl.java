package com.example.demo.services.impl;

import com.example.demo.dto.SizeDTO;
import com.example.demo.models.Size;
import com.example.demo.repository.SizeRepository;
import com.example.demo.services.SizeService;
import com.example.demo.services.mappers.SizeServiceMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SizeServiceImpl implements SizeService {
    private final SizeRepository sizeRepository;

    public SizeServiceImpl(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }

    @Override
    public ResponseEntity<?> addSize(String sizeStr) {
        if (sizeRepository.existsBySize(sizeStr)) {
            return ResponseEntity.badRequest().body("Size already exists");
        }
        Size size = Size.builder()
                .size(sizeStr)
                .build();
        sizeRepository.save(size);
        return ResponseEntity.ok().build();
    }

    @Override
    public SizeDTO getSizesById(Long id) {
        return sizeRepository.findById(id).map(SizeServiceMapper::toSizeDTO).orElse(null);
    }

    @Override
    public List<SizeDTO> getAllSizes() {
        List<Size> sizes = sizeRepository.findAll();
        return sizes.stream().map(SizeServiceMapper::toSizeDTO).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> deleteSize(Long id) {
        Size size = sizeRepository.findById(id).orElse(null);
        if (size != null) {
            sizeRepository.delete(size);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(404)
                .body("Size not found");
    }

    @Override
    public ResponseEntity<?> updateSize(Long id, String sizeStr) {
        Size size1 = sizeRepository.findById(id).orElse(null);
        if (size1 != null) {
            size1.setSize(sizeStr);
            sizeRepository.save(size1);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(404)
                .body("Size not found");
    }

}
