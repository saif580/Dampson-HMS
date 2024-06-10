package com.hms.usersmicroservice.service;

import com.hms.usersmicroservice.client.AppointmentsClient;
import com.hms.usersmicroservice.dto.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentsService {

    @Autowired
    private AppointmentsClient appointmentsClient;

    public List<Appointment> getAllAppointments() {
        return appointmentsClient.getAllAppointments();
    }
}
