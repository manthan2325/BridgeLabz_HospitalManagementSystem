
package org.example.Service;

import org.example.dto.AuthRequest;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

// Service class containing user registration business logic
@Service
public class AuthService {

    private final UserRepository userRepository;

    // Password encoder for securely hashing user passwords
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    // Constructor injection for UserRepository
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}