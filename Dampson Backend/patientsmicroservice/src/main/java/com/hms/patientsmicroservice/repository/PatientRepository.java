package com.hms.patientsmicroservice.repository;

import com.hms.patientsmicroservice.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByEmail(String email);

    boolean existsByEmailAndFirstName(String email, String firstName);
}
