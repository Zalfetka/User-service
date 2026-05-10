package com.example.food.service;

import com.example.food.dto.TokenInfo;
import com.example.food.entity.UserEntity;
import com.example.food.repository.UserRepo;
import com.example.food.security.BearerTokenGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@Slf4j
@Service
public class TokenCacheService {

    private final BearerTokenGenerator bearerTokenGenerator;
    private final Map<String, TokenInfo> tokenCache = new ConcurrentHashMap<>();
    private final UserRepo userRepo;

    @Cacheable(value = "tokens", key = "#username")
    public String getOrCreateToken(String username, Long userId) {
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String cacheKey = username + ":" + userId;

        if (tokenCache.containsKey(cacheKey)) {
            TokenInfo tokenInfo = tokenCache.get(cacheKey);
            String cachedToken = tokenInfo.getToken();

            if (bearerTokenGenerator.validateToken(cachedToken)) {
                String cachedUsername = bearerTokenGenerator.extractUsername(cachedToken);
                Long cachedUserId = bearerTokenGenerator.extractUserId(cachedToken);

                if (username.equals(cachedUsername) && userId.equals(cachedUserId)) {
                    log.debug("Returning cached valid token for user: {} (ID: {})", username, userId);
                    return cachedToken;
                }
            }

            tokenCache.remove(cacheKey);
            log.debug("Removed invalid cached token for user: {} (ID: {})", username, userId);
        }

        String newToken = bearerTokenGenerator.generateToken(user, userId);
        tokenCache.put(cacheKey, new TokenInfo(newToken, userId, LocalDate.now()));
        log.info("Generated new token for user: {} (ID: {})", username, userId);

        return newToken;
    }
}
