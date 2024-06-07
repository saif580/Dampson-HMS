package com.axis.billingmicroservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.axis.billingmicroservice.entity.Billing;
import com.axis.billingmicroservice.service.BillingService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/billings")
public class BillingController {

	@Autowired
	private BillingService billingService;

	//creating patients bill using patientId
	@PostMapping("/create")
	public Billing createBilling(@RequestBody Billing billing) {
	    return billingService.createBilling(billing.getPatientId(), billing.getAmount(), billing.getPaymentMethod());
	}
	
	//getting billing details of particular patient by using billing id
	@GetMapping("/{billingId}")
	public Optional<Billing> getBilling(@PathVariable Long billingId) {
		return billingService.getBilling(billingId);
	}

	//getting patients all Appointment Data(multiple appointments)
	@GetMapping("/patient/{patientId}")
	public List<Billing> getBillingsByPatientId(@PathVariable Long patientId) {
		return billingService.getBillingsByPatientId(patientId);
	}

	@PutMapping("/{billingId}")
	public Billing updateBillingStatus(@RequestBody Billing billing) {
		return billingService.updateBillingStatus(billing.getBillingId(), billing.getStatus());
	}
}