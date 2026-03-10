package com.example.food.security;

import com.example.food.entity.UserEntity;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Aspect
@Component
public class OwnershipAspect {

    @Before("@annotation(CheckOwnership) && args(userId,..)")
    public void checkOwnership(Long userId) throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof UserEntity currentUser) {

            if (!currentUser.getId().equals(userId)) {
                throw new AccessDeniedException("You can only access your own data");
            }
        }
    }
}
