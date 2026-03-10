package com.example.food.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class BearerTokenGenerator {

    @Value("${jwt.secret:mySecretKeyWhichShouldBeAtLeast32CharactersLong}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms:86400000}")
    private long jwtExpirationMs;

    private SecretKey signingKey;

    public String generateToken(String username, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("generatedAt", System.currentTimeMillis());

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateToken(UserDetails userDetails, Long userId) {
        return generateToken(userDetails.getUsername(), userId);
    }

    public String generateToken(String username) {
        return generateToken(username, null);
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
            signingKey = Jwts.SIG.HS256.key().build();
            jwtSecret = Base64.getEncoder().encodeToString(signingKey.getEncoded());
            log.info("Generated new JWT secret");
            return signingKey;
        }

        try {
            byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
            signingKey = Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
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
