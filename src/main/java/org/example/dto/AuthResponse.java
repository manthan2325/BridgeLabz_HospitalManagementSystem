
package org.example.dto;

import jakarta.validation.constraints.NotBlank;

// DTO for sending authentication response
public class AuthResponse {

    private String token;


    private String message;

    @NotBlank(message = "Cannot be empty")
    private String password;

    // Default constructor
    public AuthResponse() {
    }

    // Parameterized constructor
    public AuthResponse(String token,String message,String password) {
        this.token = token;
        this.message = message;
        this.password = password;
    }

    // Getter and Setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}