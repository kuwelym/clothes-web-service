package com.example.demo.services.impl;

import com.example.demo.dto.ColorDTO;
import com.example.demo.models.Color;
import com.example.demo.models.Product;
import com.example.demo.repository.ColorRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.services.ColorService;
import com.example.demo.services.mappers.ColorServiceMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColorServiceImpl implements ColorService {
    private final ColorRepository colorRepository;
    private final ProductRepository productRepository;

    public ColorServiceImpl(ColorRepository colorRepository, ProductRepository productRepository) {
        this.colorRepository = colorRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<ColorDTO> findAllColors() {
        List<Color> colorDTOList = colorRepository.findAll();
        return colorDTOList.stream()
                .map(ColorServiceMapper::toColorDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ColorDTO findColorById(Long id) {
        return colorRepository.findById(id)
                .map(ColorServiceMapper::toColorDTO)
                .orElse(null);
    }

    @Override
    public ColorDTO createColor(Long productId, String name, String hexCode) {
        Product product = productRepository
                .findById(productId)
                .orElse(null);
        if (product != null) {
            // Check if the color with the provided hexCode already exists for this product
            if (IsProductIdAndHexCodeExist(productId, hexCode)) {
                // Color with the same hexCode already exists for this product
                // You can handle this case accordingly, e.g., throw an exception or return null
                // For now, let's assume you want to return null
                return null;
            }

            // If the color with the provided hexCode does not exist, create a new one
            Color color = Color.builder()
                    .name(name)
                    .hexCode(hexCode)
                    .product(product)
                    .build();
            productUpdateColor(product, color);
            colorRepository.save(color);
            return ColorServiceMapper.toColorDTO(color);
        }
        return null;
    }


    @Override
    public ResponseEntity<?> updateColor(Long id, Long productId, String name, String hexCode) {
        Color color = colorRepository.findById(id).orElse(null);
        if (color != null) {
            Product product = productRepository
                    .findById(productId)
                    .orElse(null);
            if (product != null) {
                product.getColors().remove(color);
                color.setName(name);
                color.setHexCode(hexCode);
                color.setProduct(product);
                productUpdateColor(product, color);
                colorRepository.save(color);
                return ResponseEntity.ok(ColorServiceMapper.toColorDTO(color));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Product not found");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Color not found");
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
    public Boolean IsProductIdAndHexCodeExist(Long productId, String hexCode) {
        return colorRepository.existsByProductIdAndHexCode(productId, hexCode);
    }

    private void productUpdateColor(Product product, Color color) {
        // Get the list of colors associated with the product
        List<Color> productColors = product.getColors();

        // Check if the color to be updated already exists in the product's colors
        Color existingColor = colorRepository.findColorByProductIdAndHexCode(product.getId(), color.getHexCode());

        // If the color exists, update its properties
        if (existingColor != null) {
            existingColor.setName(color.getName());
            existingColor.setHexCode(color.getHexCode());
        } else {
            // If the color does not exist, add it to the product's colors list
            productColors.add(color);
        }
    }


    @Override
    public void deleteAllColors() {
        colorRepository.deleteAll();
    }

}
