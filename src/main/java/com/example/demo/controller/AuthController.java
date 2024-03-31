package com.example.demo.controller;

import com.example.demo.dto.ReqRes;
import com.example.demo.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

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
}

