package com.example.demo.controller;

import com.example.demo.dto.ReqRes;
import com.example.demo.repository.TokenRepository;
import com.example.demo.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private TokenRepository tokenRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ReqRes loginRequest) {
        ReqRes response = authService.login(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> signUp(@RequestBody ReqRes signUpRequest) {
        ReqRes response = authService.signUp(signUpRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/admin-register")
    public ResponseEntity<?> adminSignUp(@RequestBody ReqRes signUpRequest) {
        ReqRes response = authService.createAdmin(signUpRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody ReqRes refreshTokenRequest) {
        ReqRes response = authService.refreshToken(refreshTokenRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody ReqRes logoutRequest) {
        ReqRes response = authService.logout(logoutRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authorization){
        String token = authorization.replace("Bearer ", "");
        Boolean isValid = tokenRepository.isTokenValid(token);
        return ResponseEntity.ok().body(isValid);
    }
}

