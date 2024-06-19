package com.hms.usersmicroservice.controller;

import com.hms.usersmicroservice.client.BillingClient;
import com.hms.usersmicroservice.dto.Billing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billings")
public class BillingController {

    @Autowired
    private BillingClient billingClient;

    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'DOCTOR')")
    @PostMapping("/create")
    public Billing createBilling(@RequestBody Billing billing) {
        return billingClient.createBilling(billing);
    }

    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'DOCTOR')")
    @GetMapping("/{billingId}")
    public Billing getBilling(@PathVariable Long billingId) {
        return billingClient.getBilling(billingId);
    }

    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'DOCTOR')")
    @GetMapping("/patient/{patientId}")
    public List<Billing> getBillingsByPatientId(@PathVariable Long patientId) {
        return billingClient.getBillingsByPatientId(patientId);
    }

    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'DOCTOR')")
    @PutMapping("/{billingId}")
    public Billing updateBilling(@PathVariable Long billingId, @RequestBody Billing billing) {
        return billingClient.updateBilling(billingId, billing);
    }

    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'DOCTOR')")
    @GetMapping("/{billingId}/generatePdf")
    public ResponseEntity<byte[]> generateBillPdf(@PathVariable Long billingId) {
        byte[] pdfBytes = billingClient.generateBillPdf(billingId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bill_" + billingId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
