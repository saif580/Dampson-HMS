package com.axis.billingmicroservice.service;

import com.axis.billingmicroservice.client.PatientServiceClient;
import com.axis.billingmicroservice.dto.PatientDto;
import com.axis.billingmicroservice.entity.Billing;
import com.axis.billingmicroservice.exception.InvalidPaymentMethodException;
import com.axis.billingmicroservice.exception.NoAppointmentException;
import com.axis.billingmicroservice.repository.BillingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class BillingServiceTest {

    @Mock
    private PatientServiceClient patientServiceClient;

    @Mock
    private BillingRepository billingRepository;

    @InjectMocks
    private BillingService billingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBillings() {
        // Mock repository
        List<Billing> expectedBillings = Collections.singletonList(new Billing());
        when(billingRepository.findAll()).thenReturn(expectedBillings);

        // Call method
        List<Billing> actualBillings = billingService.getAllBillings();

        // Verify
        assertEquals(expectedBillings, actualBillings);
        verify(billingRepository, times(1)).findAll();
    }

    @Test
   
   

    void testCreateBillingWithNoPatientId() {
        // Mock data
        Long clinicId = 1L;
        Long patientId = null;
        Double amount = 100.0;
        String paymentMethod = "Online";

        // Call method and assert exception
        assertThrows(NoAppointmentException.class, () -> billingService.createBilling(clinicId, patientId, amount, paymentMethod));

        // Verify interactions
        verify(patientServiceClient, never()).getPatientById(anyLong());
        verify(billingRepository, never()).save(any(Billing.class));
    }

    @Test
    void testGetBillingById() {
        // Mock data
        Long billingId = 1L;
        Billing billing = new Billing();
        billing.setBillingId(billingId);
        when(billingRepository.findById(billingId)).thenReturn(Optional.of(billing));

        // Call method
        Optional<Billing> foundBilling = billingService.getBilling(billingId);

        // Verify
        assertTrue(foundBilling.isPresent());
        assertEquals(billingId, foundBilling.get().getBillingId());
        verify(billingRepository, times(1)).findById(billingId);
    }

    // Add more unit tests as needed for other methods in BillingService
}
