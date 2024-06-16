package com.axis.billingmicroservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.axis.billingmicroservice.entity.Billing;
import com.axis.billingmicroservice.service.BillingService;
import com.axis.billingmicroservice.service.PdfService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/billings")
public class BillingController {

	@Autowired
	private BillingService billingService;

	@Autowired
	private PdfService pdfService;

	@PostMapping("/create")
	public Billing createBilling(@RequestBody Billing billing) {
		return billingService.createBilling(
				billing.getClinicId(),
				billing.getPatientId(),
				billing.getPatientName(),
				billing.getPatientAge(),
				billing.getAmount(),
				billing.getPaymentMethod()
		);
	}

	@GetMapping("/{billingId}")
	public Optional<Billing> getBilling(@PathVariable Long billingId) {
		return billingService.getBilling(billingId);
	}

	@GetMapping("/patient/{patientId}")
	public List<Billing> getBillingsByPatientId(@PathVariable Long patientId) {
		return billingService.getBillingsByPatientId(patientId);
	}

	@PutMapping("/{billingId}")
	public Billing updateBilling(@PathVariable Long billingId, @RequestBody Billing billing) {
		return billingService.updateBilling(billingId, billing.getAmount(), billing.getPaymentMethod());
	}

	@GetMapping("/{billingId}/generatePdf")
	public ResponseEntity<byte[]> generateBillPdf(@PathVariable Long billingId) throws IOException {
		Optional<Billing> billingOptional = billingService.getBilling(billingId);
		if (billingOptional.isPresent()) {
			byte[] pdfBytes = pdfService.generateBillPdf(billingOptional.get());
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bill_" + billingId + ".pdf")
					.contentType(MediaType.APPLICATION_PDF)
					.body(pdfBytes);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/total-earnings")
	public ResponseEntity<Double> getTotalEarnings() {
		double totalEarnings = billingService.getTotalEarnings();
		return ResponseEntity.ok(totalEarnings);
	}

	@GetMapping("/payment-methods-summary")
	public ResponseEntity<Map<String, Long>> getPaymentMethodsSummary() {
		Map<String, Long> paymentMethodsSummary = billingService.getPaymentMethodsSummary();
		return ResponseEntity.ok(paymentMethodsSummary);
	}


}
