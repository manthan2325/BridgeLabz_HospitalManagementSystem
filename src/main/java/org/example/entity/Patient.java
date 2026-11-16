package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

// Marks this class as a database entity.
@Entity

// Specifies the database table name.
@Table(name = "patients")

// Generates getters and setters using Lombok.
@Getter
@Setter

// Generates a no-argument constructor required by JPA.
@NoArgsConstructor

// Generates a constructor with all fields.
@AllArgsConstructor
@Builder
public class Patient {

    // Primary key of the patients table.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Stores the patient's name.
    @Column(nullable = false)
    private String name;

    // Stores the patient's age.
    @Column(nullable = false)
    private Integer age;

    // Stores the patient's gender.
    private String gender;

    // Stores the patient's disease or medical condition.
    private String disease;

    // Stores the patient's address.
    private String address;

    // Maps Java field phoneNumber to database column phone_number.
    @Column(name = "phone_number")
    private String phoneNumber;
}