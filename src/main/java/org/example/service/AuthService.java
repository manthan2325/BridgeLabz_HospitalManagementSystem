
package org.example.service;

import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.config.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Service class containing user registration and login business logic
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Constructor injection for required dependencies
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // Register a new user
    public String registerUser(AuthRequest request) {

        // Check whether the email is already registered
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        // Create a new User entity
        User user = new User();

        // Set user details from the registration request
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // Encrypt the password before saving it
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Assign the default role to the newly registered user
        user.setRole(Role.USER);

        // Save the user details in the database
        userRepository.save(user);

        return "User registered successfully";
    }

    // Authenticate an existing user and generate JWT
    public AuthResponse loginUser(AuthRequest request) {

        // Find the user using the provided email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid email or password"));

        // Verify the entered password against the stored encrypted password
        if (!passwordEncoder.matches(
                request.getPassword(), user.getPassword())) {

            throw new IllegalArgumentException("Invalid email or password");
        }

        // Generate JWT token after successful authentication
        String token = jwtService.generateToken(user.getEmail());

        // Return the generated token
        return new AuthResponse(token);
    }
}