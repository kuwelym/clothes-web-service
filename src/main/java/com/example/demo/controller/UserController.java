package com.example.demo.controller;

import com.example.demo.config.JwtService;
import com.example.demo.services.AuthService;
import com.example.demo.services.UserService;
import com.example.demo.util.AuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private AuthService authService;
    @Autowired
    private AuthorizationUtil authorizationUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/user/favorite-products/{productId}")
    public ResponseEntity<?> addFavoriteProduct(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }
        Long userId = jwtService.extractUserIdFromToken(authorization);
        return userService.addFavoriteProduct(userId ,productId);
    }

    @DeleteMapping("/user/favorite-products/{productId}")
    public ResponseEntity<?> removeFavoriteProduct(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }
        Long userId = jwtService.extractUserIdFromToken(authorization);
        return userService.removeFavoriteProduct(userId, productId);
    }

    @GetMapping("/user/favorite-products")
    public ResponseEntity<?> getFavoriteProducts(
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }
        Long userId = jwtService.extractUserIdFromToken(authorization);
        return userService.getFavoriteProducts(userId);
    }

    @GetMapping("/user/users")
    public ResponseEntity<?> getUser(
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }

        Long userId = jwtService.extractUserIdFromToken(authorization);
        return userService.getUser(userId);
    }

}
