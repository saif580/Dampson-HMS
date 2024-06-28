package com.hms.usersmicroservice.client;


import com.hms.usersmicroservice.dto.Appointment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "appointments")
public interface AppointmentsClient {
    @GetMapping("/appointments")
    List<Appointment> getAllAppointments();
}
