package com.example.demo.services;


import com.example.demo.models.Product;
import com.example.demo.models.User;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.mappers.ProductServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserProductRepository userProductRepository;

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
        userProductRepository.save(user);
        return ResponseEntity.ok().body(ProductServiceMapper.toProductDTO(product));
    }

    public ResponseEntity<?> removeFavoriteProduct(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        Product product = productRepository.findById(productId).orElse(null);
        user.removeFavoriteProduct(product);
        userProductRepository.save(user);
        return ResponseEntity.ok().body("Product removed from favorites");
    }

    public ResponseEntity<?> getFavoriteProducts(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        return ResponseEntity.ok().body(user.getFavoriteProducts().stream().map(ProductServiceMapper::toProductDTO).collect(Collectors.toList()));
    }

}
