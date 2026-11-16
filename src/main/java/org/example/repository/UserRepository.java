package org.example.repository;

import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repository interface for User database operations.
public interface UserRepository extends JpaRepository<User, Long> {

    // Finds a user using their email address.
    Optional<User> findByEmail(String email);

    // Checks whether a user already exists with the given email.
    boolean existsByEmail(String email);
}