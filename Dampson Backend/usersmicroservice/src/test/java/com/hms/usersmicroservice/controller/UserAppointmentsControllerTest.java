package com.hms.usersmicroservice.controller;

import com.hms.usersmicroservice.dto.Appointment;
import com.hms.usersmicroservice.service.AppointmentsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class UserAppointmentsControllerTest {

    @InjectMocks
    private UserAppointmentsController userAppointmentsController;

    @Mock
    private AppointmentsService appointmentsService;

    @Mock
    private Authentication authentication;

    @SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(UserAppointmentsController.class.getName());

    private Appointment appointment;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        appointment = new Appointment("1", "John Doe", "johndoe@example.com", "2024-06-20", "09:00", "Scheduled");
    }

    @Test
    public void testGetAllAppointments() {
        List<Appointment> appointments = Arrays.asList(appointment);
        when(appointmentsService.getAllAppointments()).thenReturn(appointments);

        ResponseEntity<List<Appointment>> response = userAppointmentsController.getAllAppointments(authentication);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("John Doe", response.getBody().get(0).getName());

        verify(appointmentsService, times(1)).getAllAppointments();
    }
}
