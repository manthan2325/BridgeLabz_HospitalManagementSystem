
package org.example.controller;

import org.example.dto.PatientDto;
import org.example.service.PatientService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Enable Mockito support for JUnit 5
@ExtendWith(MockitoExtension.class)
class PatientControllerTest {

    // Mock PatientService dependency
    @Mock
    private PatientService patientService;

    // Inject mocked service into controller
    @InjectMocks
    private PatientController patientController;

    private PatientDto patientDto;

    // Initialize test datas
    @BeforeEach
    void setUp() {

        patientDto = new PatientDto();
        patientDto.setId(1L);
        patientDto.setName("Rahul Sharma");
        patientDto.setAge(25);
        patientDto.setGender("Male");
        patientDto.setDisease("Fever");
        patientDto.setAddress("Kolkata");
        patientDto.setPhoneNumber("9876543210");
    }

    // Test adding a patient
    @Test
    void testAddPatient() {

        when(patientService.addPatient(patientDto))
                .thenReturn(patientDto);

        ResponseEntity<PatientDto> response =
                patientController.addPatient(patientDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Rahul Sharma", response.getBody().getName());

        verify(patientService, times(1)).addPatient(patientDto);
    }

    // Test retrieving all patients
    @Test
    void testGetAllPatients() {

        when(patientService.getAllPatients())
                .thenReturn(Arrays.asList(patientDto));

        ResponseEntity<List<PatientDto>> response =
                patientController.getAllPatients();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());

        verify(patientService, times(1)).getAllPatients();
    }

    // Test retrieving a patient by ID
    @Test
    void testGetPatientById() {

        when(patientService.getPatientById(1L))
                .thenReturn(patientDto);

        ResponseEntity<PatientDto> response =
                patientController.getPatientById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());

        verify(patientService, times(1)).getPatientById(1L);
    }

    // Test updating a patient
    @Test
    void testUpdatePatient() {

        patientDto.setName("Amit Sharma");

        when(patientService.updatePatient(1L, patientDto))
                .thenReturn(patientDto);

        ResponseEntity<PatientDto> response =
                patientController.updatePatient(1L, patientDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Amit Sharma", response.getBody().getName());

        verify(patientService, times(1))
                .updatePatient(1L, patientDto);
    }

    // Test deleting a patient
    @Test
    void testDeletePatient() {

        doNothing().when(patientService).deletePatient(1L);

        ResponseEntity<String> response =
                patientController.deletePatient(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Patient deleted successfully", response.getBody());

        verify(patientService, times(1)).deletePatient(1L);
    }
}