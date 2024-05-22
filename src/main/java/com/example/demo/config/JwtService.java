package com.example.demo.config;

import com.example.demo.models.User;
import com.example.demo.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {
    private final SecretKey key;
    private final long EXPIRATION_TIME;
    private final long REFRESH_EXPIRATION_TIME;
    @Autowired
    TokenRepository tokenRepository;
    public JwtService(Environment environment) {
        final String SECRET_KEY = environment.getProperty("application.security.jwt.secret-key");
        assert SECRET_KEY != null;
        EXPIRATION_TIME = Long.parseLong(Objects.requireNonNull(environment.getProperty("application.security.jwt.access-token.expiration")));
        REFRESH_EXPIRATION_TIME = Long.parseLong(Objects.requireNonNull(environment.getProperty("application.security.jwt.refresh-token.expiration")));
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Long extractUserIdFromToken(String token) {
        token = token.replace("Bearer ", "");

        return extractClaims(token, claims -> Long.parseLong(claims.get("userId").toString()));
    }

    public List<String> extractAuthorities(String token) {
        token = token.replace("Bearer ", "");
        Claims claims = extractClaims(token, Function.identity());
        List<Map<String, String>> authorities = (List<Map<String, String>>) claims.get("authorities");
        List<String> roles = authorities.stream().map(auth -> auth.get("authority")).collect(Collectors.toList());
        return roles;
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public <T> T extractClaims(String token, Function <Claims, T> claimsResolver) {
        return claimsResolver.apply(
                Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
        );
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && tokenRepository.isTokenValid(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        if (!(userDetails instanceof User user)) {
            throw new IllegalArgumentException("UserDetails must be an instance of User");
        }
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("authorities", user.getAuthorities())
                .claim("userId", user.getId())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails) {
        if (!(userDetails instanceof User user)) {
            throw new IllegalArgumentException("UserDetails must be an instance of User");
        }
        claims.put("userId", user.getId());
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

}
