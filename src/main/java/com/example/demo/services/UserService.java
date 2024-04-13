package com.example.demo.services;


import com.example.demo.models.CartItem;
import com.example.demo.models.Product;
import com.example.demo.models.User;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserFavoriteProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.mappers.ProductServiceMapper;
import com.example.demo.services.mappers.UserServiceMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final UserFavoriteProductRepository userFavoriteProductRepository;

    public UserService(UserRepository userRepository, ProductRepository productRepository, UserFavoriteProductRepository userFavoriteProductRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.userFavoriteProductRepository = userFavoriteProductRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    }

    public ResponseEntity<?> addFavoriteProduct(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        Product product = productRepository.findById(productId).orElse(null);
        if(product == null){
            return ResponseEntity.badRequest().body("Product not found");
        }
        if(user.getFavoriteProducts().contains(product)){
            return ResponseEntity.badRequest().body("Product already in favorites");
        }
        user.addFavoriteProduct(product);
        userFavoriteProductRepository.save(user);
        return ResponseEntity.ok().body(ProductServiceMapper.toProductDTO(product));
    }

    public ResponseEntity<?> removeFavoriteProduct(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().body("Product not found");
        }
        user.removeFavoriteProduct(product);
        userFavoriteProductRepository.save(user);
        return ResponseEntity.ok().body("Product removed from favorites");
    }

    public ResponseEntity<?> getFavoriteProducts(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        return ResponseEntity.ok().body(user.getFavoriteProducts().stream().map(ProductServiceMapper::toProductDTO).collect(Collectors.toList()));
    }

    public ResponseEntity<?> getUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        return ResponseEntity.ok().body(UserServiceMapper.toProductDTO(user));
    }

    public ResponseEntity<?> getCartItems(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        List<CartItem> cartItems = user.getCartItems();
        return ResponseEntity.ok().body(cartItems);
    }

    public ResponseEntity<?> getCartItem(Long userId, Long id) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        CartItem cartItem = user.getCartItems().stream().filter(item -> item.getId().equals(id)).findFirst().orElse(null);
        return ResponseEntity.ok().body(cartItem);
    }

    public ResponseEntity<?> addCartItem(Long userId, Long id, int quantity) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().body("Product not found");
        }
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        user.getCartItems().add(cartItem);
        userRepository.save(user);
        return ResponseEntity.ok().body(cartItem);
    }

    public ResponseEntity<?> updateCartItem(Long userId, Long id, int quantity) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        CartItem cartItem = user.getCartItems().stream().filter(item -> item.getId().equals(id)).findFirst().orElse(null);
        if (cartItem == null) {
            return ResponseEntity.badRequest().body("Cart item not found");
        }
        cartItem.setQuantity(quantity);
        userRepository.save(user);
        return ResponseEntity.ok().body(cartItem);
    }

    public ResponseEntity<?> deleteCartItem(Long userId, Long id) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        CartItem cartItem = user.getCartItems().stream().filter(item -> item.getId().equals(id)).findFirst().orElse(null);
        if (cartItem == null) {
            return ResponseEntity.badRequest().body("Cart item not found");
        }
        user.getCartItems().remove(cartItem);
        userRepository.save(user);
        return ResponseEntity.ok().body("Cart item deleted");
    }


}
