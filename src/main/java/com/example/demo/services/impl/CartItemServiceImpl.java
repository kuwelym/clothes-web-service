package com.example.demo.services.impl;

import com.example.demo.models.*;
import com.example.demo.repository.*;
import com.example.demo.services.CartItemService;
import com.example.demo.services.mappers.CartItemServiceMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    private final UserRepository userRepository;

    private final ProductQuantityRepository productQuantityRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, UserRepository userRepository, ProductQuantityRepository productQuantityRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productQuantityRepository = productQuantityRepository;
    }


    @Override
    public ResponseEntity<?> getCartItems(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        List<CartItem> cartItems = user.getCartItems();
        return ResponseEntity.ok().body(cartItems.stream().map(CartItemServiceMapper::toCartItemDTO).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<?> addCartItem(Long userId, Long productId, int quantity, Long colorId, Long sizeId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        ProductQuantity productQuantity = productQuantityRepository.findByProductIdAndColorIdAndSizeId(productId, colorId, sizeId);
        if (productQuantity == null) {
            return ResponseEntity.badRequest().body("Product not found");
        }
        if (!isEnoughProductAvailable(productQuantity, quantity)) {
            return ResponseEntity.badRequest().body("Not enough products available");
        }
        // if the product is already in the cart, return error message
        CartItem existingCartItem = user.getCartItems()
                .stream().filter(item -> item.getProduct().getId().equals(productId)
                        && item.getSelectedColor().getId().equals(colorId)
                        && item.getSelectedSize().getId().equals(sizeId)
                ).findFirst().orElse(null);
        if (existingCartItem != null) {
            return ResponseEntity.badRequest().body("Product already in cart");
        }

        CartItem cartItem = new CartItem();
        cartItem.setProduct(productQuantity.getProduct());
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(productQuantity.getProduct().getPrice() * quantity);
        cartItem.setSelectedColor(productQuantity.getColor());
        cartItem.setSelectedSize(productQuantity.getSize());
        cartItem.setUser(user);
        user.addCartItem(cartItem);
        cartItemRepository.save(cartItem);

        return ResponseEntity.ok().body(CartItemServiceMapper.toCartItemDTO(cartItem));
    }

    @Override
    public ResponseEntity<?> updateCartItem(Long userId, Long cartItemId, int quantity) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        CartItem cartItem = user.getCartItems().stream().filter(item -> item.getId().equals(cartItemId)).findFirst().orElse(null);
        if (cartItem == null) {
            return ResponseEntity.badRequest().body("Cart item not found");
        }
        ProductQuantity productQuantity = productQuantityRepository.findByProductIdAndColorIdAndSizeId(
                cartItem.getProduct().getId(),
                cartItem.getSelectedColor().getId(),
                cartItem.getSelectedSize().getId()
        );
        if (productQuantity == null) {
            return ResponseEntity.badRequest().body("Product not found");
        }
        if (productQuantity.getQuantity() < quantity) {
            return ResponseEntity.badRequest().body("Not enough products available");
        }
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(productQuantity.getProduct().getPrice() * quantity);
        cartItemRepository.save(cartItem);
        return ResponseEntity.ok().body(CartItemServiceMapper.toCartItemDTO(cartItem));
    }

    @Override
    public ResponseEntity<?> deleteCartItem(Long userId, Long cartItemId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        CartItem cartItem = user.getCartItems().stream().filter(item -> item.getId().equals(cartItemId)).findFirst().orElse(null);
        if (cartItem == null) {
            return ResponseEntity.badRequest().body("Cart item not found");
        }
        user.removeCartItem(cartItem);
        cartItemRepository.delete(cartItem);
        return ResponseEntity.ok().body("Cart item deleted");
    }

    @Override
    public ResponseEntity<?> deleteAllCartItems(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        user.getCartItems().clear();
        userRepository.save(user);
        cartItemRepository.deleteAll();
        return ResponseEntity.ok().body("All cart items deleted");
    }

    private boolean isEnoughProductAvailable(ProductQuantity productQuantity, int quantity) {
        return productQuantity.getQuantity() >= quantity;
    }
}
