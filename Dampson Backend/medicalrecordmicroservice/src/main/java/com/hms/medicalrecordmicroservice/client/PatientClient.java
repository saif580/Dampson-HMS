package com.hms.medicalrecordmicroservice.client;

import com.hms.medicalrecordmicroservice.dto.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patientsmicroservice")
public interface PatientClient {

    @GetMapping("/patients/id/{id}")
    Patient getPatientById(@PathVariable("id") Long id);
}