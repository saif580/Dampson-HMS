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
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AppointmentServiceIntegrationTest {

    @Autowired
    private AppointmentService appointmentService;

    @MockBean
    private AppointmentRepository appointmentRepository;

    @MockBean
    private MongoTemplate mongoTemplate;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private ClinicClient clinicClient;

    @MockBean
    private PaymentService paymentService;

    private ClinicDTO clinicDTO;

    @BeforeEach
    public void setUp() {
        clinicDTO = new ClinicDTO(
                1L,
                "Dr. Smith",
                "9:00 AM - 5:00 PM",
                "Health Clinic",
                10,
                "123 Main St",
                "clinic@example.com",
                "555-1234",
                "General",
                "Monday to Friday",
                100.0,
                30,
                "general"
        );

        when(clinicClient.getClinicById(eq(1L))).thenReturn(clinicDTO);
    }

    @Test
    public void testBookAppointment() throws StripeException {
        Appointment appointment = new Appointment();
        appointment.setName("John Doe");
        appointment.setEmail("john.doe@example.com");
        appointment.setAppointmentDate(LocalDate.of(2024, 7, 1));
        appointment.setAppointmentTime(LocalTime.of(10, 0));

        Appointment savedAppointment = new Appointment();
        savedAppointment.setId("123");
        savedAppointment.setName("John Doe");
        savedAppointment.setEmail("john.doe@example.com");
        savedAppointment.setAppointmentDate(LocalDate.of(2024, 7, 1));
        savedAppointment.setAppointmentTime(LocalTime.of(10, 0));
        savedAppointment.setStatus("NOT PAID");

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedAppointment);

        Session mockSession = new Session();
        mockSession.setUrl("https://example.com/checkout");
        when(paymentService.createCheckoutSession(any(PaymentRequest.class))).thenReturn(mockSession);

        ArgumentCaptor<NotificationRequest> notificationRequestCaptor = ArgumentCaptor.forClass(NotificationRequest.class);
        when(restTemplate.postForObject(anyString(), notificationRequestCaptor.capture(), eq(String.class))).thenReturn(null);

        Appointment bookedAppointment = appointmentService.bookAppointment(appointment);

        assertNotNull(bookedAppointment.getId());
        assertEquals("NOT PAID", bookedAppointment.getStatus());
        assertEquals("john.doe@example.com", bookedAppointment.getEmail());

        List<NotificationRequest> allRequests = notificationRequestCaptor.getAllValues();
        assertEquals(2, allRequests.size());
        NotificationRequest bookingNotification = allRequests.get(0);
        NotificationRequest feeNotification = allRequests.get(1);

        assertEquals("john.doe@example.com", bookingNotification.getEmail());
        assertEquals("john.doe@example.com", feeNotification.getEmail());
    }
}
