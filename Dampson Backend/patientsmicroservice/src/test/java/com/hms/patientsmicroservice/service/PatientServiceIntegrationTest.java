package com.hms.patientsmicroservice.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.hms.patientsmicroservice.entity.Patient;
import com.hms.patientsmicroservice.repository.PatientRepository;

@SpringBootTest
@Transactional
public class PatientServiceIntegrationTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @BeforeEach
    void setUp() {
        patientRepository.deleteAll();
    }

    @Test
    void testGetAllPatients() {
        Patient patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setEmail("john.doe@example.com");
        patientRepository.save(patient);

        List<Patient> patients = patientService.getAllPatients();
        assertThat(patients).isNotEmpty();
    }

    @Test
    void testGetPatientById() {
        Patient patient = new Patient();
        patient.setFirstName("Jane");
        patient.setLastName("Doe");
        patient.setEmail("jane.doe@example.com");
        patient = patientRepository.save(patient);

        Patient foundPatient = patientService.getPatientById(patient.getPatientId());
        assertThat(foundPatient).isNotNull();
        assertThat(foundPatient.getFirstName()).isEqualTo("Jane");
    }

    @Test
    void testGetPatientByEmail() {
        Patient patient = new Patient();
        patient.setFirstName("Alice");
        patient.setLastName("Smith");
        patient.setEmail("alice.smith@example.com");
        patient = patientRepository.save(patient);

        Patient foundPatient = patientService.getPatientByEmail("alice.smith@example.com");
        assertThat(foundPatient).isNotNull();
        assertThat(foundPatient.getEmail()).isEqualTo("alice.smith@example.com");
    }

    @Test
    void testSavePatient() {
        Patient patient = new Patient();
        patient.setFirstName("Bob");
        patient.setLastName("Brown");
        patient.setEmail("bob.brown@example.com");

        Patient savedPatient = patientService.savePatient(patient);
        assertThat(savedPatient).isNotNull();
        assertThat(savedPatient.getPatientId()).isNotNull();
        assertThat(savedPatient.getFirstName()).isEqualTo("Bob");
    }

    @Test
    void testDeletePatient() {
        Patient patient = new Patient();
        patient.setFirstName("Charlie");
        patient.setLastName("Johnson");
        patient.setEmail("charlie.johnson@example.com");
        patient = patientRepository.save(patient);

        patientService.deletePatient(patient.getPatientId());

        Patient foundPatient = patientService.getPatientById(patient.getPatientId());
        assertThat(foundPatient).isNull();
    }
}
