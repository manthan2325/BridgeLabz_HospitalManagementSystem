
package org.example.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for JWT token generation and validation
class JwtServiceTest {

    private JwtService jwtService;

    private final String email = "admin@gmail.com";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();


        ReflectionTestUtils.setField(
                jwtService,
                "secret",
                "MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDE="
        );

        ReflectionTestUtils.setField(
                jwtService,
                "expiration",
                86400000L
        );
    }

    // Test successful JWT generation
    @Test
    void shouldGenerateToken() {
        String token = jwtService.generateToken(email);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    // Test extracting email from a generated JWT
    @Test
    void shouldExtractEmailFromToken() {
        String token = jwtService.generateToken(email);

        String extractedEmail = jwtService.extractUsername(token);

        assertEquals(email, extractedEmail);
    }

    // Test validating a token with the correct email
    @Test
    void shouldValidateCorrectToken() {
        String token = jwtService.generateToken(email);

        assertTrue(jwtService.isTokenValid(token, email));
    }

    // Test rejecting a token with a different email
    @Test
    void shouldRejectTokenForDifferentEmail() {
        String token = jwtService.generateToken(email);

        assertFalse(
                jwtService.isTokenValid(token, "other@gmail.com")
        );
    }

    // Test rejecting an invalid token
    @Test
    void shouldRejectInvalidToken() {
        assertFalse(
                jwtService.isTokenValid("invalid.token.value", email)
        );
    }
}