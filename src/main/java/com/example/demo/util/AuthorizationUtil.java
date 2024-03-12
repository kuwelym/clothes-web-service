package com.example.demo.util;

import com.example.demo.repository.TokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationUtil {

    private final TokenRepository tokenRepository;

    public AuthorizationUtil(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public ResponseEntity<?> validateAuthorizationHeader(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authorization.substring(7); // Remove "Bearer " prefix

        if (!tokenRepository.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return null; // Return null to indicate successful validation
    }
}
