
package org.example.config;

import org.example.entity.User;
import org.example.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

// Filter responsible for authenticating requests using JWT tokens
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    // Constructor injection for JWT service and user repository
    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    // Execute JWT authentication once for each request
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Read the Authorization header
        String authHeader = request.getHeader("Authorization");

        // Continue without authentication if the Bearer token is missing
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the JWT token after the Bearer prefix
        String token = authHeader.substring(7);

        try {
            // Extract the email stored in the token
            String email = jwtService.extractUsername(token);

            // Authenticate only if no authentication is already present
            if (email != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Find the corresponding user in the database
                Optional<User> optionalUser = userRepository.findByEmail(email);

                if (optionalUser.isPresent()) {

                    User user = optionalUser.get();

                    // Validate the token against the user's email
                    if (jwtService.isTokenValid(token, user.getEmail())) {

                        // Convert the user's role into a Spring Security authority
                        SimpleGrantedAuthority authority =
                                new SimpleGrantedAuthority(
                                        "ROLE_" + user.getRole().name());

                        // Create an authenticated security token
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        user.getEmail(),
                                        null,
                                        List.of(authority));

                        // Store authentication details for the current request
                        SecurityContextHolder.getContext()
                                .setAuthentication(authentication);
                    }
                }
            }
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException exception) {
            // Clear the security context if the JWT is invalid
            SecurityContextHolder.clearContext();
        }

        // Continue the request through the remaining filters
        filterChain.doFilter(request, response);
    }
}