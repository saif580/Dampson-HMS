package com.hms.usersmicroservice.service;

import com.hms.usersmicroservice.entity.Clinic;
import com.hms.usersmicroservice.entity.User;
import com.hms.usersmicroservice.repository.ClinicRepository;
import com.hms.usersmicroservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClinicService {
    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private UserRepository userRepository;

    public Clinic registerClinic(Clinic clinic) {
        User doctor = userRepository.findById(clinic.getDoctor().getUserId())
                .orElseThrow(() -> new AccessDeniedException("User not found"));
        if (!doctor.getRole().equals("DOCTOR")) {
            throw new AccessDeniedException("Only doctors can register clinics");
        }
        return clinicRepository.save(clinic);
    }

    public List<Clinic> findById(User doctor) {
        return clinicRepository.findByDoctor(doctor);
    }
}
