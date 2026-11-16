
package org.example.config;

import org.junit.jupiter.api.Test;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Unit tests for security configuration
class SecurityConfigTest {

    // Test BCrypt password encoder bean
    @Test
    void shouldCreatePasswordEncoder() {

        JwtAuthenticationFilter filter =
                mock(JwtAuthenticationFilter.class);

        SecurityConfig securityConfig = new SecurityConfig(filter);

        PasswordEncoder encoder = securityConfig.passwordEncoder();

        assertNotNull(encoder);
        assertTrue(encoder.matches(
                "Admin@123",
                encoder.encode("Admin@123")
        ));
    }

    // Test security filter chain creation
    @Test
    void shouldCreateSecurityFilterChain() throws Exception {

        JwtAuthenticationFilter filter =
                mock(JwtAuthenticationFilter.class);

        SecurityConfig securityConfig = new SecurityConfig(filter);

        SecurityFilterChain filterChain =
                securityConfig.securityFilterChain(
                        org.springframework.security.config.annotation.web.builders.HttpSecurity
                                .class.cast(
                                createHttpSecurity(filter)
                        )
                );

        assertNotNull(filterChain);
    }

    // Create HttpSecurity through Spring's configuration helper
    private org.springframework.security.config.annotation.web.builders.HttpSecurity
    createHttpSecurity(JwtAuthenticationFilter filter) {

        org.springframework.security.config.annotation.web.builders.HttpSecurity http =
                mock(
                        org.springframework.security.config.annotation.web.builders.HttpSecurity.class,
                        RETURNS_DEEP_STUBS
                );

        return http;
    }
}