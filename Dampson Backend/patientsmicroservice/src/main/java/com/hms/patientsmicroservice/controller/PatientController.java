package com.hms.patientsmicroservice.controller;

import com.hms.patientsmicroservice.entity.Patient;
import com.hms.patientsmicroservice.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);

    @Autowired
    private PatientService patientService;

    @GetMapping
    public List<Patient> getAllPatients() {
        logger.debug("Fetching all patients");
        return patientService.getAllPatients();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        logger.debug("Fetching patient with ID: {}", id);
        try {
            Patient patient = patientService.getPatientById(id);
            return ResponseEntity.ok(patient);
        } catch (Exception e) {
            logger.error("Error fetching patient with ID: {}", id, e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Patient> getPatientByEmail(@PathVariable String email) {
        logger.debug("Fetching patient with email: {}", email);
        try {
            Patient patient = patientService.getPatientByEmail(email);
            return ResponseEntity.ok(patient);
        } catch (Exception e) {
            logger.error("Error fetching patient with email: {}", email, e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Patient> registerPatient(@RequestBody Patient patient) {
        logger.debug("Registering patient: {}", patient);
        try {
            Patient registeredPatient = patientService.savePatient(patient);
            return ResponseEntity.ok(registeredPatient);
        } catch (Exception e) {
            logger.error("Error registering patient: {}", patient, e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        logger.debug("Deleting patient with ID: {}", id);
        try {
            patientService.deletePatient(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting patient with ID: {}", id, e);
            return ResponseEntity.status(500).build();
        }
    }
}