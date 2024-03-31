package com.example.demo.controller;

import com.example.demo.dto.SizeDTO;
import com.example.demo.services.SizeService;
import com.example.demo.util.AuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SizeController {
    private final SizeService sizeService;
    @Autowired
    private AuthorizationUtil authorizationUtil;

    public SizeController(SizeService sizeService) {
        this.sizeService = sizeService;
    }

    @GetMapping("/public/sizes/{id}")
    public ResponseEntity<?> getSizeById(@PathVariable Long id) {
        SizeDTO size = sizeService.getSizesById(id);
        if(size == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Size not found");
        }
        return ResponseEntity.ok(size);
    }

    @GetMapping("/public/sizes")
    public ResponseEntity<?> getAllSizes() {
        return ResponseEntity.ok(sizeService.getAllSizes());
    }

    @GetMapping("/public/sizes/color/{colorId}")
    public ResponseEntity<?> getSizesByColorId(@PathVariable Long colorId) {
        List<SizeDTO> sizes = sizeService.getSizesByColorId(colorId);
        if(sizes == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Sizes not found");
        }
        return ResponseEntity.ok(sizes);
    }



    @DeleteMapping("/admin/sizes/{id}")
    public ResponseEntity<?> deleteSize(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }
        return sizeService.deleteSize(id);
    }

    @PatchMapping("/admin/sizes/{id}")
    public ResponseEntity<?> updateSize(
            @PathVariable Long id,
            @RequestBody SizeDTO sizeDTO,
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }
        return sizeService.updateSize(id, sizeDTO.getSize());
    }

    @PostMapping("/admin/sizes")
    public ResponseEntity<?> addSize(
            @RequestBody SizeDTO sizeDTO,
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }

        return sizeService.addSize(sizeDTO.getSize(), sizeDTO.getColorId());
    }
}
