package com.example.food.security;

import com.example.food.entity.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Component
@Slf4j
public class BearerTokenGenerator {

    @Value("${jwt.secret:mySecretKeyWhichShouldBeAtLeast32CharactersLong}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms:86400000}")
    private long jwtExpirationMs;

    private SecretKey signingKey;

    public String generateToken(UserEntity user, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", user.getUsername());
        claims.put("generatedAt", System.currentTimeMillis());
        claims.put("roles", List.of(user.getRole().name()));

        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public Long extractUserId(String token) {
        try {
            return extractClaim(token, claims -> claims.get("userId", Long.class));
        } catch (Exception e) {
            log.debug("Failed to extract userId from token: {}", e.getMessage());
            return null;
        }
    }

    public String extractUsername(String token) {
        try {
            String username = extractClaim(token, claims -> claims.get("username", String.class));
            return username != null ? username : extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            log.debug("Failed to extract username from token: {}", e.getMessage());
            return null;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.debug("Token expired: {}", e.getMessage());
            throw e;
        } catch (JwtException e) {
            log.debug("Invalid token: {}", e.getMessage());
            throw e;
        }
    }

    private SecretKey getSigningKey() {

        if (signingKey != null) {
            return signingKey;
        }

        if (jwtSecret == null || jwtSecret.isBlank()) {
            throw new IllegalStateException("jwt.secret is not configured");
        }

        try {
            byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(jwtSecret);
            signingKey = io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);

        } catch (IllegalArgumentException e) {
            signingKey = io.jsonwebtoken.security.Keys.hmacShaKeyFor(
                    jwtSecret.getBytes(java.nio.charset.StandardCharsets.UTF_8)
            );
        }
        return signingKey;
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String username = claims.get("username", String.class);
            return username != null ? username : claims.getSubject();
        }catch (Exception e) {
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.debug("Invalid JWT signature: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.debug("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.debug("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.debug("JWT claims string is empty: {}", e.getMessage());
        } catch (JwtException e) {
            log.debug("JWT validation failed: {}", e.getMessage());
        }
        return false;
    }
}
