
package org.example.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

// Service class responsible for JWT token operations
@Service
public class JwtService {

    // Secret key stored in application properties in Base64 format
    @Value("${jwt.secret}")
    private String secret;

    // Token expiration time in milliseconds
    @Value("${jwt.expiration}")
    private long expiration;

    // Convert the Base64 secret into a cryptographic signing key
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate a JWT token using the user's email
    public String generateToken(String email) {

        Date currentDate = new Date();

        // Calculate the token expiration time
        Date expiryDate = new Date(currentDate.getTime() + expiration);

        // Build and sign the JWT token
        return Jwts.builder()
                .subject(email)
                .issuedAt(currentDate)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    // Extract the email (subject) from the JWT token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Extract all claims after verifying the token signature
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Check whether the token belongs to the given email and is not expired
    public boolean isTokenValid(String token, String email) {
        try {
            String extractedEmail = extractUsername(token);

            return extractedEmail.equals(email)
                    && !extractAllClaims(token).getExpiration().before(new Date());

        } catch (JwtException | IllegalArgumentException exception) {
            // Invalid, malformed, or expired tokens are rejected
            return false;
        }
    }
}