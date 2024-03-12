package com.example.demo.controller;

import com.example.demo.dto.ReqRes;
import com.example.demo.services.AuthService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private AuthService authService;

    @PostMapping("/public/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ReqRes changePasswordRequest) {
        ReqRes response = authService.changePassword(changePasswordRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/public/logout")
    public ResponseEntity<?> logout(@RequestBody ReqRes logoutRequest) {
        ReqRes response = authService.logout(logoutRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
