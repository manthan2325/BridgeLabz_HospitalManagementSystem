
package org.example.service;

import org.example.dto.PatientDto;
import org.example.entity.Patient;
import org.example.repository.PatientRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// Service class containing patient management business logic
@Service
public class PatientService {

    private final PatientRepository patientRepository;

    // Constructor injection for PatientRepository
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // Add a new patient
    public PatientDto addPatient(PatientDto patientDto) {

        // Convert DTO into Patient entity
        Patient patient = convertToEntity(patientDto);

        // Save patient details in the database
        Patient savedPatient = patientRepository.save(patient);

        // Convert saved entity into DTO and return it
        return convertToDto(savedPatient);
    }

    // Retrieve all patients
    public List<PatientDto> getAllPatients() {

        // Fetch all patient records from the database
        return patientRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Retrieve a patient using their ID
    public PatientDto getPatientById(Long id) {

        // Find patient or throw exception if not found
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Patient not found with ID: " + id));

        return convertToDto(patient);
    }

    // Update an existing patient's details
    public PatientDto updatePatient(Long id, PatientDto patientDto) {

        // Find the existing patient
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Patient not found with ID: " + id));

        // Update patient details
        existingPatient.setName(patientDto.getName());
        existingPatient.setAge(patientDto.getAge());
        existingPatient.setGender(patientDto.getGender());
        existingPatient.setDisease(patientDto.getDisease());
        existingPatient.setAddress(patientDto.getAddress());
        existingPatient.setPhoneNumber(patientDto.getPhoneNumber());

        // Save updated patient details
        Patient updatedPatient = patientRepository.save(existingPatient);

        return convertToDto(updatedPatient);
    }

    // Delete a patient using their ID
    public void deletePatient(Long id) {

        // Check whether the patient exists
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Patient not found with ID: " + id));

        // Delete the patient record
        patientRepository.delete(patient);
    }

    // Convert PatientDto into Patient entity
    private Patient convertToEntity(PatientDto patientDto) {

        Patient patient = new Patient();

        patient.setName(patientDto.getName());
        patient.setAge(patientDto.getAge());
        patient.setGender(patientDto.getGender());
        patient.setDisease(patientDto.getDisease());
        patient.setAddress(patientDto.getAddress());
        patient.setPhoneNumber(patientDto.getPhoneNumber());

        return patient;
    }

    // Convert Patient entity into PatientDto
    private PatientDto convertToDto(Patient patient) {

        PatientDto patientDto = new PatientDto();

        patientDto.setId(patient.getId());
        patientDto.setName(patient.getName());
        patientDto.setAge(patient.getAge());
        patientDto.setGender(patient.getGender());
        patientDto.setDisease(patient.getDisease());
        patientDto.setAddress(patient.getAddress());
        patientDto.setPhoneNumber(patient.getPhoneNumber());

        return patientDto;
    }
}