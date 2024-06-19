package com.hms.usersmicroservice.service;

import com.hms.usersmicroservice.client.AppointmentsClient;
import com.hms.usersmicroservice.dto.Appointment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AppointmentsServiceTest {

    @Mock
    private AppointmentsClient appointmentsClient;

    @InjectMocks
    private AppointmentsService appointmentsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllAppointments() {
        // Given
        Appointment appointment1 = new Appointment("1", "John Doe", "john.doe@example.com", "2024-06-20", "10:00 AM", "Scheduled");
        Appointment appointment2 = new Appointment("2", "Jane Smith", "jane.smith@example.com", "2024-06-21", "02:00 PM", "Cancelled");
        List<Appointment> appointments = Arrays.asList(appointment1, appointment2);

        // Mock behavior of appointmentsClient
        when(appointmentsClient.getAllAppointments()).thenReturn(appointments);

        // When
        List<Appointment> result = appointmentsService.getAllAppointments();

        // Then
        assertEquals(2, result.size());
        assertEquals(appointment1, result.get(0));
        assertEquals(appointment2, result.get(1));

        // Verify interactions
        verify(appointmentsClient, times(1)).getAllAppointments();
        verifyNoMoreInteractions(appointmentsClient);
    }
}
