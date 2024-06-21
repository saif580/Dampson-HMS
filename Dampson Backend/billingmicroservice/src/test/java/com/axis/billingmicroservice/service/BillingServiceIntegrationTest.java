package com.axis.billingmicroservice.service;

import com.axis.billingmicroservice.client.PatientServiceClient;
import com.axis.billingmicroservice.dto.PatientDto;
import com.axis.billingmicroservice.entity.Billing;
import com.axis.billingmicroservice.repository.BillingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class BillingServiceIntegrationTest {

    @Autowired
    private BillingService billingService;

    @MockBean
    private PatientServiceClient patientServiceClient;

    @Autowired
    private BillingRepository billingRepository;

    @BeforeEach
    void setUp() {
        billingRepository.deleteAll(); // Clear existing data before each test
    }

    @Test
    void testCreateBilling() {
        // Mock data
        Long clinicId = 1L;
        Long patientId = 1L;
        Double amount = 100.0;
        String paymentMethod = "Online";

        PatientDto patientDto = new PatientDto();
        patientDto.setPatientId(patientId);
        patientDto.setFirstName("John");
        patientDto.setLastName("Doe");
        patientDto.setEmail("john.doe@example.com");

        when(patientServiceClient.getPatientById(patientId)).thenReturn(patientDto);

        // Call method
        Billing billing = billingService.createBilling(clinicId, patientId, amount, paymentMethod);

        // Fetch from repository to verify
        Optional<Billing> savedBillingOptional = billingRepository.findById(billing.getBillingId());

        // Verify
        assertEquals(true, savedBillingOptional.isPresent());
        Billing savedBilling = savedBillingOptional.get();
        assertEquals(clinicId, savedBilling.getClinicId());
        assertEquals(patientId, savedBilling.getPatientId());
        assertEquals("John", savedBilling.getPatientFirstName());
        assertEquals("Doe", savedBilling.getPatientLastName());
        assertEquals("john.doe@example.com", savedBilling.getPatientEmail());
        assertEquals(amount, savedBilling.getAmount());
        assertEquals(paymentMethod, savedBilling.getPaymentMethod());
        assertNotNull(savedBilling.getPaymentDate());
    }

    @Test
    void testUpdateBilling() {
        // Create a billing first
        Long clinicId = 1L;
        Long patientId = 1L;
        Double amount = 100.0;
        String paymentMethod = "Online";

        PatientDto patientDto = new PatientDto();
        patientDto.setPatientId(patientId);
        patientDto.setFirstName("John");
        patientDto.setLastName("Doe");
        patientDto.setEmail("john.doe@example.com");

        when(patientServiceClient.getPatientById(patientId)).thenReturn(patientDto);

        Billing billing = billingService.createBilling(clinicId, patientId, amount, paymentMethod);

        // Update the billing
        Double updatedAmount = 150.0;
        String updatedPaymentMethod = "Cash";

        Billing updatedBilling = billingService.updateBilling(billing.getBillingId(), updatedAmount, updatedPaymentMethod);

        // Fetch from repository to verify
        Optional<Billing> fetchedBillingOptional = billingRepository.findById(updatedBilling.getBillingId());

        // Verify
        assertEquals(true, fetchedBillingOptional.isPresent());
        Billing fetchedBilling = fetchedBillingOptional.get();
        assertEquals(updatedAmount, fetchedBilling.getAmount());
        assertEquals(updatedPaymentMethod, fetchedBilling.getPaymentMethod());
    }

    // Add more integration tests as needed for other methods in BillingService
}
