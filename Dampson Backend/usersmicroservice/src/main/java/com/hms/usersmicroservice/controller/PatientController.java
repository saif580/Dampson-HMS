package com.hms.usersmicroservice.controller;

import com.hms.usersmicroservice.client.PatientClient;
import com.hms.usersmicroservice.dto.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientClient patientClient;

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientClient.getAllPatients();
    }

    @GetMapping("id/{id}")
    public Patient getPatientById(@PathVariable Long id) {
        return patientClient.getPatientById(id);
    }

    @GetMapping("/email/{email}")
    public Patient getPatientByEmail(@PathVariable String email) {
        return patientClient.getPatientByEmail(email);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerPatient(@RequestBody Patient patient) {
        try {
            Patient registeredPatient = patientClient.registerPatient(patient);
            return ResponseEntity.ok("Patient registered successfully.");
        } catch (feign.FeignException.BadRequest e) {
            return ResponseEntity.status(400).body(e.contentUTF8()); // Pass the error message from patientsmicroservice
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An internal server error occurred. Please try again later.");
        }
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @DeleteMapping("id/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientClient.deletePatient(id);
    }
}
