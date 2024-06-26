package com.example.demo.services;

import com.example.demo.config.JwtService;
import com.example.demo.dto.ReqRes;
import com.example.demo.models.Token;
import com.example.demo.models.User;
import com.example.demo.models.enums.Role;
import com.example.demo.models.enums.TokenType;
import com.example.demo.repository.TokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.InputValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ReqRes login(ReqRes signInRequest) {
        ReqRes response = new ReqRes();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInRequest.getUsername(),
                            signInRequest.getPassword()
                    )
            );
            var user = userRepository.findByUsername(signInRequest.getUsername()).orElseThrow();
            var jwt = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwt);

            response.setStatusCode(200);
            response.setMessage("User signed in successfully");
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24h");

        } catch (Exception e) {
            response.setStatusCode(401);
            response.setMessage("Invalid username or password");
        }
        return response;
    }

    public ReqRes signUp(ReqRes signUpRequest) {
        ReqRes response = new ReqRes();

        if (!InputValidator.isValidEmail(signUpRequest.getEmail())) {
            response.setStatusCode(400);
            response.setMessage("Invalid email format");
            return response;
        }

        if (!InputValidator.isValidPassword(signUpRequest.getPassword())) {
            response.setStatusCode(400);
            response.setMessage("Invalid password format. Password must contain at least one uppercase letter and be at least 8 characters long");
            return response;
        }

        if (!InputValidator.isValidPhoneNumber(signUpRequest.getPhoneNum())) {
            response.setStatusCode(400);
            response.setMessage("Invalid phone number format");
            return response;
        }

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            response.setStatusCode(400);
            response.setMessage("Username is already taken");
            return response;
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            response.setStatusCode(400);
            response.setMessage("Email is already taken");
            return response;
        }
        if (userRepository.existsByPhoneNumber(signUpRequest.getPhoneNum())) {
            response.setStatusCode(400);
            response.setMessage("Phone number is already taken");
            return response;
        } else {
            User user = User.builder()
                    .username(signUpRequest.getUsername())
                    .password(passwordEncoder.encode(signUpRequest.getPassword()))
                    .phoneNumber(signUpRequest.getPhoneNum())
                    .email(signUpRequest.getEmail())
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
            var jwt = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
            saveUserToken(user, jwt);

            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setMessage("User registered successfully");
        }
        return response;
    }

    public ReqRes createAdmin(ReqRes signUpRequest) {
        ReqRes response = new ReqRes();

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            response.setStatusCode(400);
            response.setMessage("Username is already taken");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            response.setStatusCode(400);
            response.setMessage("Email is already taken");
        }
        if (userRepository.existsByPhoneNumber(signUpRequest.getPhoneNum())) {
            response.setStatusCode(400);
            response.setMessage("Phone number is already taken");
        } else {
            User user = User.builder()
                    .username(signUpRequest.getUsername())
                    .password(passwordEncoder.encode(signUpRequest.getPassword()))
                    .phoneNumber(signUpRequest.getPhoneNum())
                    .email(signUpRequest.getEmail())
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(user);
            var jwt = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
            saveUserToken(user, jwt);

            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setMessage("Admin registered successfully");
        }
        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes response = new ReqRes();
        try {
            String username = jwtService.extractUsername(refreshTokenRequest.getRefreshToken());
            User user = userRepository.findByUsername(username).orElseThrow();
            if (jwtService.isTokenValid(refreshTokenRequest.getRefreshToken(), user)) {
                var jwt = jwtService.generateToken(user);
                var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

                revokeAllUserTokens(user);
                saveUserToken(user, jwt);

                response.setStatusCode(200);
                response.setMessage("Token refreshed successfully");
                response.setToken(jwt);
                response.setRefreshToken(refreshToken);
                response.setExpirationTime("24h");
            } else {
                response.setStatusCode(400);
                response.setMessage("Invalid refresh token");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error");
        }
        return response;
    }

    public ReqRes logout(ReqRes logoutRequest) {
        ReqRes response = new ReqRes();

        try {

            String username = jwtService.extractUsername(logoutRequest.getToken());
            User user = userRepository.findByUsername(username).orElseThrow();
            if (jwtService.isTokenValid(logoutRequest.getToken(), user)) {
                var token = tokenRepository.findByToken(logoutRequest.getToken()).orElseThrow();
                token.setExpired(true);
                token.setRevoked(true);
                tokenRepository.save(token);
                response.setStatusCode(200);
                response.setMessage("User logged out successfully");
            } else {
                response.setStatusCode(400);
                response.setMessage("Invalid token");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error");
        }
        return response;
    }

    public ReqRes changePassword(ReqRes changePasswordRequest) {
        ReqRes response = new ReqRes();
        try {
            String username = jwtService.extractUsername(changePasswordRequest.getToken());
            User user = userRepository.findByUsername(username).orElseThrow();
            if (jwtService.isTokenValid(changePasswordRequest.getToken(), user)) {
                user.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
                userRepository.save(user);
                response.setStatusCode(200);
                response.setMessage("Password changed successfully");
            } else {
                response.setStatusCode(400);
                response.setMessage("Invalid token");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error");
        }
        return response;
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}
