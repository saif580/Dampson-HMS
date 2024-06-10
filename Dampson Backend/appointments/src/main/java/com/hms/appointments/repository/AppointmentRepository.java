package com.hms.appointments.repository;

import com.hms.appointments.entity.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    List<Appointment> findByEmail(String email);
}
