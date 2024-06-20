package com.hms.patientsmicroservice.controller;

import com.hms.patientsmicroservice.entity.Patient;
import com.hms.patientsmicroservice.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PatientControllerTest {

    @InjectMocks
    private PatientController patientController;

    @Mock
    private PatientService patientService;

    private Patient patient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        patient = new Patient();
        patient.setPatientId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setEmail("john.doe@example.com");
        patient.setPhone("1234567890");
        patient.setAddress("123 Main St");
        patient.setDateOfBirth(new Date());
    }

    @Test
    public void testGetAllPatients() {
        when(patientService.getAllPatients()).thenReturn(Collections.singletonList(patient));

        List<Patient> result = patientController.getAllPatients();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(patientService, times(1)).getAllPatients();
    }

    @Test
    public void testGetPatientById_Success() {
        when(patientService.getPatientById(anyLong())).thenReturn(patient);

        ResponseEntity<Patient> response = patientController.getPatientById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John", response.getBody().getFirstName());
        verify(patientService, times(1)).getPatientById(anyLong());
    }

    @Test
    public void testGetPatientById_Failure() {
        when(patientService.getPatientById(anyLong())).thenThrow(new RuntimeException("Patient not found"));

        ResponseEntity<Patient> response = patientController.getPatientById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(patientService, times(1)).getPatientById(anyLong());
    }

    @Test
    public void testGetPatientByEmail_Success() {
        when(patientService.getPatientByEmail(anyString())).thenReturn(patient);

        ResponseEntity<Patient> response = patientController.getPatientByEmail("john.doe@example.com");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John", response.getBody().getFirstName());
        verify(patientService, times(1)).getPatientByEmail(anyString());
    }

    @Test
    public void testGetPatientByEmail_Failure() {
        when(patientService.getPatientByEmail(anyString())).thenThrow(new RuntimeException("Patient not found"));

        ResponseEntity<Patient> response = patientController.getPatientByEmail("john.doe@example.com");

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(patientService, times(1)).getPatientByEmail(anyString());
    }

    @Test
    public void testRegisterPatient_Success() {
        when(patientService.savePatient(any(Patient.class))).thenReturn(patient);

        ResponseEntity<String> response = patientController.registerPatient(patient);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Patient registered successfully.", response.getBody());
        verify(patientService, times(1)).savePatient(any(Patient.class));
    }

    @Test
    public void testRegisterPatient_DuplicateFirstName() {
        when(patientService.savePatient(any(Patient.class))).thenThrow(new IllegalArgumentException("Duplicate first name"));

        ResponseEntity<String> response = patientController.registerPatient(patient);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Duplicate first name", response.getBody());
        verify(patientService, times(1)).savePatient(any(Patient.class));
    }

    @Test
    public void testRegisterPatient_InternalServerError() {
        when(patientService.savePatient(any(Patient.class))).thenThrow(new RuntimeException("Internal error"));

        ResponseEntity<String> response = patientController.registerPatient(patient);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An internal server error occurred. Please try again later.", response.getBody());
        verify(patientService, times(1)).savePatient(any(Patient.class));
    }

    @Test
    public void testDeletePatient_Success() {
        doNothing().when(patientService).deletePatient(anyLong());

        ResponseEntity<Void> response = patientController.deletePatient(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(patientService, times(1)).deletePatient(anyLong());
    }

    @Test
    public void testDeletePatient_Failure() {
        doThrow(new RuntimeException("Internal error")).when(patientService).deletePatient(anyLong());

        ResponseEntity<Void> response = patientController.deletePatient(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(patientService, times(1)).deletePatient(anyLong());
    }
}
