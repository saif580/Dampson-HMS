package com.axis.billingmicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hms.patientsmicroservice.entity.Patient;


@Service
public class PatientServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    public Patient getPatientById(Long patientId) {
        String url = "http://localhost:7002/patients/id/" + patientId;
        return restTemplate.getForObject(url, Patient.class);
    }
}
