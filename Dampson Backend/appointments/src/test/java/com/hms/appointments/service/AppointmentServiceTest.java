package com.hms.appointments.service;

import com.hms.appointments.client.ClinicClient;
import com.hms.appointments.dto.ClinicDTO;
import com.hms.appointments.dto.NotificationRequest;
import com.hms.appointments.dto.PaymentRequest;
import com.hms.appointments.entity.Appointment;
import com.hms.appointments.repository.AppointmentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ClinicClient clinicClient;

    @Mock
    private PaymentService paymentService;

    private Appointment appointment;
    private ClinicDTO clinic;

    @BeforeEach
    void setUp() {
        appointment = new Appointment();
        appointment.setName("John Doe");
        appointment.setEmail("john.doe@example.com");
        appointment.setAppointmentDate(LocalDate.of(2024, 6, 20));
        appointment.setAppointmentTime(LocalTime.of(10, 0));

        clinic = new ClinicDTO();
        clinic.setClinicTime("9:00 AM - 5:00 PM");
        clinic.setOperatingDays("Monday to Friday");
        clinic.setMaxPatientsPerHour(5);
        clinic.setDoctorFees(100.0);
        clinic.setClinicName("Health Clinic");
        clinic.setDoctorName("Dr. Smith");
        clinic.setContactNumber("123456789");
        clinic.setEmail("clinic@example.com");
        clinic.setClinicSpeciality("General Medicine");
        clinic.setAddress("123 Main St");

        when(clinicClient.getClinicById(anyLong())).thenReturn(clinic);
    }

    @Test
    void testBookAppointment_Success() throws StripeException {
        when(appointmentRepository.findByEmail(anyString())).thenReturn(new ArrayList<>());
        when(mongoTemplate.count(any(Query.class), eq(Appointment.class))).thenReturn(0L);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(paymentService.createCheckoutSession(any(PaymentRequest.class))).thenReturn(new Session());

        Appointment result = appointmentService.bookAppointment(appointment);

        assertNotNull(result);
        verify(appointmentRepository, times(1)).save(appointment);
        verify(restTemplate, times(2)).postForObject(anyString(), any(NotificationRequest.class), eq(String.class));
    }

    @Test
    void testBookAppointment_ClinicNotFound() {
        when(clinicClient.getClinicById(anyLong())).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.bookAppointment(appointment);
        });

        assertEquals("Clinic not found", exception.getMessage());
    }

    @Test
    void testBookAppointment_DuplicateAppointment() {
        List<Appointment> existingAppointments = new ArrayList<>();
        existingAppointments.add(appointment);
        when(appointmentRepository.findByEmail(anyString())).thenReturn(existingAppointments);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.bookAppointment(appointment);
        });

        assertEquals("Cannot book multiple appointments with the same name and email on the same day", exception.getMessage());
    }

    @Test
    void testBookAppointment_InvalidTime() {
        appointment.setAppointmentTime(LocalTime.of(8, 0));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.bookAppointment(appointment);
        });

        assertEquals("Appointment time is outside clinic operating hours. Please choose a time between 9:00 AM - 5:00 PM", exception.getMessage());
    }

    @Test
    void testBookAppointment_MaxPatientsReached() {
        when(mongoTemplate.count(any(Query.class), eq(Appointment.class))).thenReturn(5L);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.bookAppointment(appointment);
        });

        assertEquals("Maximum number of patients per hour reached for the selected time slot. Please choose a different time.", exception.getMessage());
    }
}
