
package org.example.service;

import org.example.dto.PatientDto;
import org.example.entity.Patient;
import org.example.repository.PatientRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Enable Mockito support for JUnit 5
@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    // Mock the PatientRepository dependency
    @Mock
    private PatientRepository patientRepository;

    // Inject mocked repository into PatientService
    @InjectMocks
    private PatientService patientService;

    private Patient patient;
    private PatientDto patientDto;

    // Initialize test data before every test
    @BeforeEach
    void setUp() {

        patient = new Patient();
        patient.setId(1L);
        patient.setName("Rahul Sharma");
        patient.setAge(25);
        patient.setGender("Male");
        patient.setDisease("Fever");
        patient.setAddress("Kolkata");
        patient.setPhoneNumber("9876543210");

        patientDto = new PatientDto();
        patientDto.setId(1L);
        patientDto.setName("Rahul Sharma");
        patientDto.setAge(25);
        patientDto.setGender("Male");
        patientDto.setDisease("Fever");
        patientDto.setAddress("Kolkata");
        patientDto.setPhoneNumber("9876543210");
    }

    // Test adding a new patient
    @Test
    void testAddPatient() {

        when(patientRepository.save(any(Patient.class)))
                .thenReturn(patient);

        PatientDto result = patientService.addPatient(patientDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Rahul Sharma", result.getName());

        verify(patientRepository, times(1))
                .save(any(Patient.class));
    }

    // Test retrieving all patients
    @Test
    void testGetAllPatients() {

        when(patientRepository.findAll())
                .thenReturn(Arrays.asList(patient));

        List<PatientDto> result = patientService.getAllPatients();

        assertEquals(1, result.size());
        assertEquals("Rahul Sharma", result.get(0).getName());

        verify(patientRepository, times(1)).findAll();
    }

    // Test retrieving a patient by ID
    @Test
    void testGetPatientById() {

        when(patientRepository.findById(1L))
                .thenReturn(Optional.of(patient));

        PatientDto result = patientService.getPatientById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Rahul Sharma", result.getName());

        verify(patientRepository, times(1)).findById(1L);
    }

    // Test retrieving a patient that does not exist
    @Test
    void testGetPatientByIdNotFound() {

        when(patientRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> patientService.getPatientById(99L));

        verify(patientRepository, times(1)).findById(99L);
    }

    // Test updating an existing patient
    @Test
    void testUpdatePatient() {

        patientDto.setName("Amit Sharma");

        when(patientRepository.findById(1L))
                .thenReturn(Optional.of(patient));

        when(patientRepository.save(any(Patient.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PatientDto result = patientService.updatePatient(1L, patientDto);

        assertEquals("Amit Sharma", result.getName());

        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    // Test updating a patient that does not exist
    @Test
    void testUpdatePatientNotFound() {

        when(patientRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> patientService.updatePatient(99L, patientDto));

        verify(patientRepository, never()).save(any(Patient.class));
    }

    // Test deleting an existing patient
    @Test
    void testDeletePatient() {

        when(patientRepository.findById(1L))
                .thenReturn(Optional.of(patient));

        doNothing().when(patientRepository).delete(patient);

        assertDoesNotThrow(() -> patientService.deletePatient(1L));

        verify(patientRepository, times(1)).delete(patient);
    }

    // Test deleting a patient that does not exist
    @Test
    void testDeletePatientNotFound() {

        when(patientRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> patientService.deletePatient(99L));

        verify(patientRepository, never()).delete(any(Patient.class));
    }
}