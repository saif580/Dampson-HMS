package com.hms.usersmicroservice.controller;


import com.hms.usersmicroservice.dto.Appointment;
import com.hms.usersmicroservice.service.AppointmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user-appointments")
public class UserAppointmentsController {

    @Autowired
    private AppointmentsService appointmentsService;

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments(Authentication authentication) {
        // You can add any additional checks or processing here based on the user's role or authentication
        List<Appointment> appointments = appointmentsService.getAllAppointments();
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }
}