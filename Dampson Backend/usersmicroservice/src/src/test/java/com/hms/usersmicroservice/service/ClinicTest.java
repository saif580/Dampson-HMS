package com.hms.usersmicroservice.service;

import org.junit.jupiter.api.Test;

import com.hms.usersmicroservice.entity.Clinic;
import com.hms.usersmicroservice.entity.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ClinicTest {

    @Test
    public void testConstructorAndGetters() {
        // Create test data
        Long clinicId = 1L;
        User doctor = new User();
        String doctorName = "Doctor Name";
        String clinicName = "Clinic Name";
        String clinicTime = "Clinic Time";
        int maxPatientsPerHour = 10;
        String address = "Clinic Address";
        String contactNumber = "1234567890";
        String email = "clinic@example.com";
        String clinicSpeciality = "Speciality";
        String clinicFacilities = "Facilities";
        String operatingDays = "Monday to Friday";
        int appointmentDuration = 30;
        double doctorFees = 100.0;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // Create Clinic instance using the constructor
        Clinic clinic = new Clinic(clinicId, doctor, doctorName, clinicName, clinicTime, maxPatientsPerHour,
                address, contactNumber, email, clinicSpeciality, clinicFacilities, operatingDays,
                appointmentDuration, doctorFees, createdAt, updatedAt);

        // Assert the values initialized by the constructor
        assertEquals(clinicId, clinic.getClinicId());
        assertEquals(doctor, clinic.getDoctor());
        assertEquals(doctorName, clinic.getDoctorName());
        assertEquals(clinicName, clinic.getClinicName());
        assertEquals(clinicTime, clinic.getClinicTime());
        assertEquals(maxPatientsPerHour, clinic.getMaxPatientsPerHour());
        assertEquals(address, clinic.getAddress());
        assertEquals(contactNumber, clinic.getContactNumber());
        assertEquals(email, clinic.getEmail());
        assertEquals(clinicSpeciality, clinic.getClinicSpeciality());
        assertEquals(clinicFacilities, clinic.getClinicFacilities());
        assertEquals(operatingDays, clinic.getOperatingDays());
        assertEquals(appointmentDuration, clinic.getAppointmentDuration());
        assertEquals(doctorFees, clinic.getDoctorFees());
        assertEquals(createdAt, clinic.getCreatedAt());
        assertEquals(updatedAt, clinic.getUpdatedAt());
    }

    @Test
    public void testSetters() {
        // Create Clinic instance
        Clinic clinic = new Clinic();

        // Set values using setters
        clinic.setClinicId(1L);
        clinic.setDoctor(new User());
        clinic.setDoctorName("Doctor Name");
        clinic.setClinicName("Clinic Name");
        clinic.setClinicTime("Clinic Time");
        clinic.setMaxPatientsPerHour(10);
        clinic.setAddress("Clinic Address");
        clinic.setContactNumber("1234567890");
        clinic.setEmail("clinic@example.com");
        clinic.setClinicSpeciality("Speciality");
        clinic.setClinicFacilities("Facilities");
        clinic.setOperatingDays("Monday to Friday");
        clinic.setAppointmentDuration(30);
        clinic.setDoctorFees(100.0);
        clinic.setCreatedAt(LocalDateTime.now());
        clinic.setUpdatedAt(LocalDateTime.now());

        // Assert values using getters
        assertEquals(1L, clinic.getClinicId());
        assertEquals("Doctor Name", clinic.getDoctorName());
        assertEquals("Clinic Name", clinic.getClinicName());
        assertEquals("Clinic Time", clinic.getClinicTime());
        assertEquals(10, clinic.getMaxPatientsPerHour());
        assertEquals("Clinic Address", clinic.getAddress());
        assertEquals("1234567890", clinic.getContactNumber());
        assertEquals("clinic@example.com", clinic.getEmail());
        assertEquals("Speciality", clinic.getClinicSpeciality());
        assertEquals("Facilities", clinic.getClinicFacilities());
        assertEquals("Monday to Friday", clinic.getOperatingDays());
        assertEquals(30, clinic.getAppointmentDuration());
        assertEquals(100.0, clinic.getDoctorFees());
        assertNotNull(clinic.getCreatedAt());
        assertNotNull(clinic.getUpdatedAt());
    }
}
