
package org.example.controller;

import org.example.dto.PatientDto;
import org.example.service.PatientService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST controller for patient management APIs
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    // Constructor injection for PatientService
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // Add a new patient
    @PostMapping
    public ResponseEntity<PatientDto> addPatient(
            @Valid @RequestBody PatientDto patientDto) {

        // Call service to save patient details
        PatientDto savedPatient = patientService.addPatient(patientDto);

        // Return 201 Created response
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
    }

    // Retrieve all patients
    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {

        // Fetch all patient records
        List<PatientDto> patients = patientService.getAllPatients();

        return ResponseEntity.ok(patients);
    }

    // Retrieve a patient using their ID
    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long id) {

        // Fetch patient details by ID
        PatientDto patient = patientService.getPatientById(id);

        return ResponseEntity.ok(patient);
    }

    // Update an existing patient's details
    @PutMapping("/{id}")
    public ResponseEntity<PatientDto> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientDto patientDto) {

        // Update patient details using service layer
        PatientDto updatedPatient =
                patientService.updatePatient(id, patientDto);

        return ResponseEntity.ok(updatedPatient);
    }

    // Delete a patient using their ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {

        // Delete patient record
        patientService.deletePatient(id);

        return ResponseEntity.ok("Patient deleted successfully");
    }
}