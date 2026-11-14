package org.example.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    // Tests the User entity constructor and getter methods.
    @Test
    void shouldCreateUserSuccessfully() {

        User user = new User(
                1L,
                "Admin User",
                "admin@gmail.com",
                "encodedPassword",
                Role.ADMIN
        );

        assertEquals(1L, user.getId());
        assertEquals("Admin User", user.getName());
        assertEquals("admin@gmail.com", user.getEmail());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(Role.ADMIN, user.getRole());
    }

    // Tests updating User entity fields using setters.
    @Test
    void shouldUpdateUserDetails() {

        User user = new User();

        user.setName("Test User");
        user.setEmail("test@gmail.com");
        user.setRole(Role.USER);

        assertEquals("Test User", user.getName());
        assertEquals("test@gmail.com", user.getEmail());
        assertEquals(Role.USER, user.getRole());
    }

    // Tests the Patient entity constructor and getter methods.
    @Test
    void shouldCreatePatientSuccessfully() {

        Patient patient = new Patient(
                1L,
                "Rahul Kumar",
                27,
                "Male",
                "Fever",
                "Chennai",
                "9876543210"
        );

        assertEquals(1L, patient.getId());
        assertEquals("Rahul Kumar", patient.getName());
        assertEquals(27, patient.getAge());
        assertEquals("Male", patient.getGender());
        assertEquals("Fever", patient.getDisease());
        assertEquals("Chennai", patient.getAddress());
        assertEquals("9876543210", patient.getPhoneNumber());
    }

    // Tests updating Patient entity fields using setters.
    @Test
    void shouldUpdatePatientDetails() {

        Patient patient = new Patient();

        patient.setName("Amit Sharma");
        patient.setAge(30);
        patient.setDisease("Viral Fever");

        assertEquals("Amit Sharma", patient.getName());
        assertEquals(30, patient.getAge());
        assertEquals("Viral Fever", patient.getDisease());
    }

    // Tests the available user roles.
    @Test
    void shouldVerifyAvailableRoles() {

        assertEquals(2, Role.values().length);
        assertEquals(Role.ADMIN, Role.valueOf("ADMIN"));
        assertEquals(Role.USER, Role.valueOf("USER"));
    }
}