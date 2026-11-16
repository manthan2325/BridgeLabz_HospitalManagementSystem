package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

// Marks this class as a database entity.
@Entity

// Specifies the database table name.
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        }
)

// Generates getters and setters using Lombok.
@Getter
@Setter

// Generates a no-argument constructor required by JPA.
@NoArgsConstructor

// Generates a constructor with all fields.
@AllArgsConstructor
@Builder
public class User {

    // Primary key of the users table.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Stores the user's full name.
    @Column(nullable = false)
    private String name;

    // Stores a unique email address.
    @Column(nullable = false, unique = true)
    private String email;

    // Stores the encoded password.
    @Column(nullable = false)
    private String password;

    // Stores the role as text instead of an ordinal number.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
