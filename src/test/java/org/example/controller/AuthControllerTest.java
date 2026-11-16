
package org.example.controller;

import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.service.AuthService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Enable Mockito support for JUnit 5
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    // Mock AuthService dependency
    @Mock
    private AuthService authService;

    // Inject mocked service into controller
    @InjectMocks
    private AuthController authController;

    // Test user registration endpoint
    @Test
    void testRegisterUser() {

        AuthRequest request = new AuthRequest();
        request.setName("Admin User");
        request.setEmail("admin@gmail.com");
        request.setPassword("Admin@123");

        when(authService.registerUser(request))
                .thenReturn("User registered successfully");

        ResponseEntity<String> response =
                authController.registerUser(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());

        verify(authService, times(1)).registerUser(request);
    }

    // Test user login endpoint
    @Test
    void testLoginUser() {

        AuthRequest request = new AuthRequest();
        request.setEmail("admin@gmail.com");
        request.setPassword("Admin@123");

        AuthResponse authResponse = new AuthResponse("mock-jwt-token");

        when(authService.loginUser(request))
                .thenReturn(authResponse);

        ResponseEntity<AuthResponse> response =
                authController.loginUser(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("mock-jwt-token", response.getBody().getToken());

        verify(authService, times(1)).loginUser(request);
    }
}