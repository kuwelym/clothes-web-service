package com.example.demo.util;

import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ValidationUtils {
    private final UserRepository userRepository;

    public ValidationUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> validateUserExistence(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        return ResponseEntity.ok(user);
    }
}
