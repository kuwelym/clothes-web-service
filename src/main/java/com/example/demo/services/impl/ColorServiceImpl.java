package com.example.demo.services.impl;

import com.example.demo.dto.ColorDTO;
import com.example.demo.models.Color;
import com.example.demo.repository.ColorRepository;
import com.example.demo.services.ColorService;
import com.example.demo.services.mappers.ColorServiceMapper;
import com.example.demo.util.ColorUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColorServiceImpl implements ColorService {
    private final ColorRepository colorRepository;
    public ColorServiceImpl(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    @Override
    public List<ColorDTO> findAllColors() {
        List<Color> colorDTOList = colorRepository.findAll();
        return colorDTOList.stream()
                .map(ColorServiceMapper::toColorDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ColorDTO createColor(String hexCode) {
        if (colorRepository.existsByHexCode(hexCode)) {
            return null;
        }
        Color color = Color.builder()
                .hexCode(hexCode)
                .name(ColorUtils.getColorNameFromHex(Integer.parseInt(hexCode.substring(1), 16)))
                .build();
        colorRepository.save(color);
        return ColorServiceMapper.toColorDTO(color);
    }


    @Override
    public ResponseEntity<?> updateColor(Long id, String hexCode) {
        Color color = colorRepository.findById(id).orElse(null);
        if (color == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Color not found");
        }
        color.setHexCode(hexCode);
        color.setName(ColorUtils.getColorNameFromHex(Integer.parseInt(hexCode.substring(1), 16)));
        colorRepository.save(color);
        return ResponseEntity.ok(ColorServiceMapper.toColorDTO(color));
    }

    @Override
    public ResponseEntity<?> deleteColor(Long id) {
        Color color = colorRepository.findById(id).orElse(null);
        if (color != null) {
            colorRepository.delete(color);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Color not found");
    }


    @Override
    public void deleteAllColors() {
        colorRepository.deleteAll();
    }

}
