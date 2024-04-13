package com.example.demo.controller;

import com.example.demo.config.JwtService;
import com.example.demo.dto.CartItemDTO;
import com.example.demo.services.AuthService;
import com.example.demo.services.CartItemService;
import com.example.demo.services.UserService;
import com.example.demo.util.AuthorizationUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final AuthService authService;
    private final AuthorizationUtil authorizationUtil;
    private final UserService userService;
    private final JwtService jwtService;
    private final CartItemService cartItemService;

    public UserController(AuthService authService, AuthorizationUtil authorizationUtil, UserService userService, JwtService jwtService, CartItemService cartItemService) {
        this.authService = authService;
        this.authorizationUtil = authorizationUtil;
        this.userService = userService;
        this.jwtService = jwtService;
        this.cartItemService = cartItemService;
    }

    @PostMapping("/favorite-products/{productId}")
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

    @DeleteMapping("/favorite-products/{productId}")
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

    @GetMapping("/favorite-products")
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

    @GetMapping("/users")
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

    @GetMapping("/carts")
    public ResponseEntity<?> getCart(
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }

        Long userId = jwtService.extractUserIdFromToken(authorization);
        return cartItemService.getCartItems(userId);
    }

    @PostMapping("/carts")
    public ResponseEntity<?> addToCart(
            @RequestBody CartItemDTO cartItemDTO,
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }

        Long userId = jwtService.extractUserIdFromToken(authorization);
        return cartItemService.addCartItem(userId, cartItemDTO.getProductId(), cartItemDTO.getQuantity(), cartItemDTO.getSelectedColorId(), cartItemDTO.getSelectedSizeId());
    }

    @PatchMapping("/carts/{cartItemId}")
    public ResponseEntity<?> updateCartItem(
            @RequestBody CartItemDTO cartItemDTO,
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }

        Long userId = jwtService.extractUserIdFromToken(authorization);
        return cartItemService.updateCartItem(userId, cartItemId, cartItemDTO.getQuantity());
    }

    @DeleteMapping("/carts/{cartItemId}")
    public ResponseEntity<?> removeFromCart(
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }

        Long userId = jwtService.extractUserIdFromToken(authorization);
        return cartItemService.deleteCartItem(userId, cartItemId);
    }

    @DeleteMapping("/carts")
    public ResponseEntity<?> removeAllCartItems(
            @RequestHeader("Authorization") String authorization
    ){
        ResponseEntity<?> response = authorizationUtil.validateAuthorizationHeader(authorization);
        if (response != null) {
            return response;
        }

        Long userId = jwtService.extractUserIdFromToken(authorization);
        return cartItemService.deleteAllCartItems(userId);
    }

}
