package com.hms.usersmicroservice.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TokenBlacklistServiceTest {

    private TokenBlacklistService tokenBlacklistService;

    @BeforeEach
    public void setUp() {
        tokenBlacklistService = new TokenBlacklistService();
    }

    @Test
    public void testBlacklistToken() {
        // Arrange
        String token = "abc123";

        // Act
        tokenBlacklistService.blacklistToken(token);

        // Assert
        assertTrue(tokenBlacklistService.isBlacklisted(token));
    }

    @Test
    public void testIsBlacklisted() {
        // Arrange
        String token = "xyz456";

        // Act
        boolean initiallyBlacklisted = tokenBlacklistService.isBlacklisted(token);
        tokenBlacklistService.blacklistToken(token);
        boolean afterBlacklisting = tokenBlacklistService.isBlacklisted(token);

        // Assert
        assertFalse(initiallyBlacklisted);
        assertTrue(afterBlacklisting);
    }

    @Test
    public void testIsBlacklisted_NotBlacklisted() {
        // Arrange
        String token = "notblacklistedtoken";

        // Act
        boolean result = tokenBlacklistService.isBlacklisted(token);

        // Assert
        assertFalse(result);
    }
}
