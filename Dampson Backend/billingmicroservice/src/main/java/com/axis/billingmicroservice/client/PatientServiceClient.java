package com.axis.billingmicroservice.client;

import com.axis.billingmicroservice.dto.PatientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patientsmicroservice")
public interface PatientServiceClient {

    @GetMapping("/patients/id/{patientId}")
    PatientDto getPatientById(@PathVariable("patientId") Long patientId);
}