
package org.example.controller;

import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// REST controller for user registration and login
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // Constructor injection for AuthService
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @Valid @RequestBody AuthRequest request) {

        // Call service to register the user
        String response = authService.registerUser(request);

        // Return 201 Created response
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Login an existing user
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(
            @Valid @RequestBody AuthRequest request) {

        // Authenticate user and generate JWT
        AuthResponse response = authService.loginUser(request);

        // Return 200 OK with JWT token
        return ResponseEntity.ok(response);
    }
}