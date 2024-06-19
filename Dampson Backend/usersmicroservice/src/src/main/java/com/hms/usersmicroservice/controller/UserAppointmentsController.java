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
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/user-appointments")
public class UserAppointmentsController {

    private static final Logger logger = Logger.getLogger(UserAppointmentsController.class.getName());

    @Autowired
    private AppointmentsService appointmentsService;

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments(Authentication authentication) {
        logger.info("Received request to get all appointments");
        logger.info("Authentication: " + authentication);

        List<Appointment> appointments = appointmentsService.getAllAppointments();
        logger.info("Returning appointments: " + appointments);

        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }
}
