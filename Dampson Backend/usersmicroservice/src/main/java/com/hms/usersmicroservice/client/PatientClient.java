package com.hms.usersmicroservice.client;

import com.hms.usersmicroservice.dto.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "patientsmicroservice")
public interface PatientClient {
    @GetMapping("/patients")
    List<Patient> getAllPatients();

    @GetMapping("/patients/id/{id}")
    Patient getPatientById(@PathVariable("id") Long id);

    @GetMapping("/patients/email/{email}")
    Patient getPatientByEmail(@PathVariable("email") String email);

    @PostMapping("/patients/register")
    Patient registerPatient(@RequestBody Patient patient);

    @DeleteMapping("/patients/id/{id}")
    void deletePatient(@PathVariable("id") Long id);
}