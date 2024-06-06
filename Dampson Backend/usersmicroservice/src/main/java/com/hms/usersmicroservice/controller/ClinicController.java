package com.hms.usersmicroservice.controller;

import com.hms.usersmicroservice.entity.Clinic;
import com.hms.usersmicroservice.entity.User;
import com.hms.usersmicroservice.service.ClinicService;
import com.hms.usersmicroservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clinics")
public class ClinicController {
    @Autowired
    private ClinicService clinicService;

    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Clinic> registerClinic(@Valid @RequestBody Clinic clinic, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.findByUsername(userDetails.getUsername());
        clinic.setDoctor(currentUser);
        Clinic createdClinic = clinicService.registerClinic(clinic);
        return new ResponseEntity<>(createdClinic, HttpStatus.CREATED);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Clinic>> getClinicsByDoctor(@PathVariable Long doctorId) {
        User doctor = userService.getUserById(doctorId);
        List<Clinic> clinics = clinicService.findById(doctor);
        return new ResponseEntity<>(clinics, HttpStatus.OK);
    }

    // Other endpoints if needed
}
