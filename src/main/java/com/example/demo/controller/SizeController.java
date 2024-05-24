package com.example.demo.controller;

import com.example.demo.dto.SizeDTO;
import com.example.demo.services.SizeService;
import com.example.demo.util.AuthorizationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SizeController {
    private final SizeService sizeService;
    private final AuthorizationUtil authorizationUtil;

    public SizeController(SizeService sizeService, AuthorizationUtil authorizationUtil) {
        this.sizeService = sizeService;
        this.authorizationUtil = authorizationUtil;
    }

    @GetMapping("/sizes/{id}")
    public ResponseEntity<?> getSizeById(@PathVariable Long id) {
        SizeDTO size = sizeService.getSizesById(id);
        if(size == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Size not found");
        }
        return ResponseEntity.ok(size);
    }

    @GetMapping("/sizes")
    public ResponseEntity<?> getAllSizes() {
        return ResponseEntity.ok(sizeService.getAllSizes());
    }

    @DeleteMapping("/sizes/{id}")
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

    @PatchMapping("/sizes/{id}")
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

    @PostMapping("/sizes")
    public ResponseEntity<?> addSize(
            @RequestBody SizeDTO sizeDTO,
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }

        return sizeService.addSize(sizeDTO.getSize());
    }
}
