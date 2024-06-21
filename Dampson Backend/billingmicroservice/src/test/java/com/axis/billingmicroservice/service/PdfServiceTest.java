package com.axis.billingmicroservice.service;

import com.axis.billingmicroservice.entity.Billing;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PdfServiceTest {

    @Mock
    private Billing mockBilling;

    @InjectMocks
    private PdfService pdfService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateBillPdf() throws IOException {
        // Setup mock Billing object
        long billingId = 1L;
        long clinicId = 1L;
        long patientId = 1L;
        String patientFirstName = "John";
        String patientLastName = "Doe";
        String patientEmail = "john.doe@example.com";
        Double amount = 100.0;
        LocalDateTime paymentDate = LocalDateTime.now();
        String paymentMethod = "Credit Card";

        when(mockBilling.getBillingId()).thenReturn(billingId);
        when(mockBilling.getClinicId()).thenReturn(clinicId);
        when(mockBilling.getPatientId()).thenReturn(patientId);
        when(mockBilling.getPatientFirstName()).thenReturn(patientFirstName);
        when(mockBilling.getPatientLastName()).thenReturn(patientLastName);
        when(mockBilling.getPatientEmail()).thenReturn(patientEmail);
        when(mockBilling.getAmount()).thenReturn(amount);
        when(mockBilling.getPaymentDate()).thenReturn(paymentDate);
        when(mockBilling.getPaymentMethod()).thenReturn(paymentMethod);

        // Generate PDF bytes
        byte[] pdfBytes = pdfService.generateBillPdf(mockBilling);

        // Assert that PDF bytes are not null and not empty
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);

        // Verify the content of the PDF (optional, depending on your needs)
        verify(mockBilling, atLeastOnce()).getBillingId();
        verify(mockBilling, atLeastOnce()).getClinicId();
        verify(mockBilling, atLeastOnce()).getPatientId();
        verify(mockBilling, atLeastOnce()).getPatientFirstName();
        verify(mockBilling, atLeastOnce()).getPatientLastName();
        verify(mockBilling, atLeastOnce()).getPatientEmail();
        verify(mockBilling, atLeastOnce()).getAmount();
        verify(mockBilling, atLeastOnce()).getPaymentDate();
        verify(mockBilling, atLeastOnce()).getPaymentMethod();

        // Optional: Verify PDF content if needed
        // verifyPdfContent(pdfBytes);
    }
}
