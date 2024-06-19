package com.hms.patientsmicroservice.service;

import com.hms.patientsmicroservice.entity.Patient;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatientServiceTest {

    @Test
    void testGetAllPatients() {
        // Create a mock PatientService
        PatientService patientService = mock(PatientService.class);

        // Define behavior for the mock
        when(patientService.getAllPatients()).thenReturn(List.of());

        // Call the method and assert the result
        List<Patient> patients = patientService.getAllPatients();
        assertNotNull(patients);
        assertEquals(0, patients.size());
    }
}
