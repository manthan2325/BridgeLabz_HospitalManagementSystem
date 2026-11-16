
package org.example.service;

import org.example.config.JwtService;
import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Enable Mockito support for JUnit 5
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    // Mock repository dependency
    @Mock
    private UserRepository userRepository;

    // Mock password encoder
    @Mock
    private PasswordEncoder passwordEncoder;

    // Mock JWT service
    @Mock
    private JwtService jwtService;

    // Inject mocks into AuthService
    @InjectMocks
    private AuthService authService;

    private AuthRequest authRequest;
    private User user;

    // Initialize test data
    @BeforeEach
    void setUp() {

        authRequest = new AuthRequest();
        authRequest.setName("Admin User");
        authRequest.setEmail("admin@gmail.com");
        authRequest.setPassword("Admin@123");

        user = new User();
        user.setId(1L);
        user.setName("Admin User");
        user.setEmail("admin@gmail.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);
    }

    // Test successful user registration
    @Test
    void testRegisterUser() {

        when(userRepository.existsByEmail("admin@gmail.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("Admin@123"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        String result = authService.registerUser(authRequest);

        assertEquals("User registered successfully", result);

        // Capture saved user details
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();

        assertEquals("Admin User", savedUser.getName());
        assertEquals("admin@gmail.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(Role.USER, savedUser.getRole());
    }

    // Test registration with an already registered email
    @Test
    void testRegisterDuplicateEmail() {

        when(userRepository.existsByEmail("admin@gmail.com"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> authService.registerUser(authRequest));

        verify(userRepository, never()).save(any(User.class));
    }

    // Test successful login
    @Test
    void testLoginUser() {

        when(userRepository.findByEmail("admin@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("Admin@123", "encodedPassword"))
                .thenReturn(true);

        when(jwtService.generateToken("admin@gmail.com"))
                .thenReturn("mock-jwt-token");

        AuthResponse response = authService.loginUser(authRequest);

        assertNotNull(response);
        assertEquals("mock-jwt-token", response.getToken());

        verify(jwtService).generateToken("admin@gmail.com");
    }

    // Test login with an email that does not exist
    @Test
    void testLoginUserNotFound() {

        when(userRepository.findByEmail("admin@gmail.com"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> authService.loginUser(authRequest));

        verify(jwtService, never()).generateToken(anyString());
    }

    // Test login with an incorrect password
    @Test
    void testLoginWrongPassword() {

        when(userRepository.findByEmail("admin@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("Admin@123", "encodedPassword"))
                .thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> authService.loginUser(authRequest));

        verify(jwtService, never()).generateToken(anyString());
    }
}