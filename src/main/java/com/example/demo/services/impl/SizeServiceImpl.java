package com.example.demo.services.impl;

import com.example.demo.dto.SizeDTO;
import com.example.demo.models.Color;
import com.example.demo.models.Size;
import com.example.demo.repository.ColorRepository;
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
    private final ColorRepository colorRepository;

    public SizeServiceImpl(SizeRepository sizeRepository, ColorRepository colorRepository) {
        this.sizeRepository = sizeRepository;
        this.colorRepository = colorRepository;
    }

    @Override
    public ResponseEntity<?> addSize(String sizeStr, Long colorId) {
        Color color = colorRepository.findById(colorId).orElse(null);
        if (color != null) {
            if (IsSizeExist(sizeStr, color)) {
                return ResponseEntity.status(409)
                        .body("Size already exists");
            }
            Size size = Size.builder()
                    .size(sizeStr)
                    .color(color)
                    .build();
            sizeRepository.save(size);
            return ResponseEntity.ok(SizeServiceMapper.toSizeDTO(size));
        }
        return ResponseEntity.status(404)
                .body("Color not found");
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

    private boolean IsSizeExist(String sizeStr, Color color) {
        List<Size> sizes = sizeRepository.findAll();
        for (Size size : sizes) {
            if (size.getSize().equals(sizeStr) && size.getColor().getId().equals(color.getId())) {
                return true;
            }
        }
        return false;
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
    public ResponseEntity<?> updateSize(Long id, String size) {
        Size size1 = sizeRepository.findById(id).orElse(null);
        if (size1 != null) {
            size1.setSize(size);
            sizeRepository.save(size1);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(404)
                .body("Size not found");
    }

    @Override
    public List<SizeDTO> getSizesByColorId(Long colorId) {
        List<Size> sizes = sizeRepository.findAllByColorId(colorId);
        return sizes
                .stream()
                .map(SizeServiceMapper::toSizeDTO)
                .collect(Collectors.toList());
    }
}
