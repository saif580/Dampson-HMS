package com.axis.billingmicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.axis.billingmicroservice.entity.Billing;
import com.axis.billingmicroservice.exception.InvalidPaymentMethodException;
import com.axis.billingmicroservice.exception.NoAppointmentException;
import com.axis.billingmicroservice.repository.BillingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BillingService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private BillingRepository billingRepository;

	private static final List<String> VALID_PAYMENT_METHODS = List.of("Online", "Cash", "Prepaid");

	public Billing createBilling(Long clinicId, Long patientId, String patientName, String patientAge, Double amount, String paymentMethod) {

		if (!VALID_PAYMENT_METHODS.contains(paymentMethod)) {
			throw new InvalidPaymentMethodException(paymentMethod);
		}

		if (patientId != null) {
			Billing billing = new Billing();
			billing.setClinicId(clinicId);
			billing.setPatientId(patientId);
			billing.setPatientName(patientName);
			billing.setPatientAge(patientAge);
			billing.setAmount(amount);
			billing.setPaymentDate(LocalDateTime.now());
			billing.setPaymentMethod(paymentMethod);

			return billingRepository.save(billing);
		} else {
			throw new NoAppointmentException(patientId);
		}
	}

	public Optional<Billing> getBilling(Long billingId) {
		return billingRepository.findById(billingId);
	}

	public List<Billing> getBillingsByPatientId(Long patientId) {
		return billingRepository.findByPatientId(patientId);
	}

	public Billing updateBilling(Long billingId, Double amount, String paymentMethod) {
		Optional<Billing> optionalBilling = billingRepository.findById(billingId);
		if (optionalBilling.isPresent()) {
			Billing billing = optionalBilling.get();
			billing.setAmount(amount);
			billing.setPaymentMethod(paymentMethod);
			return billingRepository.save(billing);
		}
		return null;
	}
}
