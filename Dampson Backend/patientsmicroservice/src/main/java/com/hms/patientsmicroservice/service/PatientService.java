package com.hms.patientsmicroservice.service;

import com.hms.patientsmicroservice.entity.Patient;
import com.hms.patientsmicroservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId).orElse(null);
    }

    public Patient getPatientByEmail(String email) {
        List<Patient> patients = patientRepository.findByEmail(email);
        return patients.isEmpty() ? null : patients.get(0);
    }

    public Patient savePatient(Patient patient) {
        if (patientRepository.existsByEmailAndFirstName(patient.getEmail(), patient.getFirstName())) {
            throw new IllegalArgumentException("A patient with the same first name already exists for this email.");
        }
        return patientRepository.save(patient);
    }

    public void deletePatient(Long patientId) {
        patientRepository.deleteById(patientId);
    }
}
