package com.example.demo.services;


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

}
