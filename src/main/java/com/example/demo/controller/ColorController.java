package com.example.demo.controller;

import com.example.demo.dto.ColorDTO;
import com.example.demo.services.ColorService;
import com.example.demo.util.AuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class ColorController {
    private final ColorService colorService;
    private final AuthorizationUtil authorizationUtil;

    @Autowired
    public ColorController(ColorService colorService, AuthorizationUtil authorizationUtil) {
        this.colorService = colorService;
        this.authorizationUtil = authorizationUtil;
    }

    @PostMapping("/colors")
    public ResponseEntity<?> createColor(
            @RequestBody ColorDTO colorDTO,
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }
        ColorDTO color = colorService.createColor(colorDTO.getHexCode());
        if (color == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Color with the same hexCode already exists for this product");
        }
        return ResponseEntity.ok(color);
    }

    @GetMapping("/colors")
    public List<ColorDTO> getColors() {
        return colorService.findAllColors();
    }

    @GetMapping("/colors/{id}")
    public ResponseEntity<?> getColor(
            @PathVariable Long id
    ){
        return colorService.findColorById(id);
    }

    @PatchMapping("/colors/{id}")
    public ResponseEntity<?> updateColor(
            @PathVariable Long id,
            @RequestBody ColorDTO colorDTO,
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }
        return colorService.updateColor(id, colorDTO.getHexCode());
    }

    @DeleteMapping("/colors/{id}")
    public ResponseEntity<?> deleteColor(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }
        return colorService.deleteColor(id);
    }

    @DeleteMapping("/colors")
    public ResponseEntity<?> deleteAllColors(
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }
        colorService.deleteAllColors();
        return ResponseEntity.ok().build();
    }
}
