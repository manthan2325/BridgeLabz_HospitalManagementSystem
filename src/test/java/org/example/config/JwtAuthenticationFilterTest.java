
package org.example.config;

import org.example.entity.Role;
import org.example.entity.User;
import org.example.repository.UserRepository;

import jakarta.servlet.FilterChain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Unit tests for JWT authentication filter
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtFilter;

    @BeforeEach
    void setUp() {
        jwtFilter = new JwtAuthenticationFilter(
                jwtService,
                userRepository
        );

        // Clear previous authentication before each test
        SecurityContextHolder.clearContext();
    }

    // Test request without Authorization header
    @Test
    void shouldContinueWhenTokenIsMissing() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService, userRepository);
    }

    // Test request with a valid JWT token
    @Test
    void shouldAuthenticateValidToken() throws Exception {

        String email = "admin@gmail.com";
        String token = "valid-token";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        MockHttpServletResponse response = new MockHttpServletResponse();

        User user = new User();
        user.setEmail(email);
        user.setRole(Role.USER);

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(token, email)).thenReturn(true);

        jwtFilter.doFilter(request, response, filterChain);

        assertNotNull(
                SecurityContextHolder.getContext().getAuthentication()
        );

        assertEquals(
                email,
                SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal()
        );

        verify(filterChain).doFilter(request, response);
    }

    // Test request with an invalid JWT token
    @Test
    void shouldRejectInvalidToken() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalid-token");

        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.extractUsername("invalid-token"))
                .thenThrow(new IllegalArgumentException("Invalid token"));

        jwtFilter.doFilter(request, response, filterChain);

        assertNull(
                SecurityContextHolder.getContext().getAuthentication()
        );

        verify(filterChain).doFilter(request, response);
    }

    // Test request when the user does not exist in the database
    @Test
    void shouldNotAuthenticateUnknownUser() throws Exception {

        String email = "unknown@gmail.com";
        String token = "valid-token";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        jwtFilter.doFilter(request, response, filterChain);

        assertNull(
                SecurityContextHolder.getContext().getAuthentication()
        );

        verify(filterChain).doFilter(request, response);
    }
}