package com.hms.patientsmicroservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hms.patientsmicroservice.entity.Patient;
import com.hms.patientsmicroservice.repository.PatientRepository;

public class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPatients() {
        when(patientRepository.findAll()).thenReturn(Collections.emptyList());

        List<Patient> patients = patientService.getAllPatients();
        assertNotNull(patients);
        assertEquals(0, patients.size());
    }

    @Test
    void testGetPatientById() {
        Long patientId = 1L;
        Patient patient = new Patient();
        patient.setPatientId(patientId);

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        Patient foundPatient = patientService.getPatientById(patientId);
        assertNotNull(foundPatient);
        assertEquals(patientId, foundPatient.getPatientId());
    }

    @Test
    void testGetPatientByEmail() {
        String email = "test@example.com";
        Patient patient = new Patient();
        patient.setEmail(email);

        when(patientRepository.findByEmail(email)).thenReturn(patient);

        Patient foundPatient = patientService.getPatientByEmail(email);
        assertNotNull(foundPatient);
        assertEquals(email, foundPatient.getEmail());
    }

    @Test
    void testSavePatient() {
        Patient patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");

        when(patientRepository.save(patient)).thenReturn(patient);

        Patient savedPatient = patientService.savePatient(patient);
        assertNotNull(savedPatient);
        assertEquals("John", savedPatient.getFirstName());
    }

    @Test
    void testDeletePatient() {
        Long patientId = 1L;

        doNothing().when(patientRepository).deleteById(patientId);

        patientService.deletePatient(patientId);
        verify(patientRepository, times(1)).deleteById(patientId);
    }
}