package com.example.demo.controller;

import com.example.demo.models.User;
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

    @PostMapping("/user/favorite-products/{productId}")
    public ResponseEntity<?> addFavoriteProduct(
            @RequestBody User user,
            @PathVariable Long productId,
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }
        return userService.addFavoriteProduct(user.getId() ,productId);
    }

    @DeleteMapping("/user/favorite-products/{productId}")
    public ResponseEntity<?> removeFavoriteProduct(
            @RequestBody User user,
            @PathVariable Long productId,
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }
        return userService.removeFavoriteProduct(user.getId(), productId);
    }

    @GetMapping("/user/favorite-products")
    public ResponseEntity<?> getFavoriteProducts(
            @RequestBody User user,
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }
        return userService.getFavoriteProducts(user.getId());
    }

}
