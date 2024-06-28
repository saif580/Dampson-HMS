package com.axis.billingmicroservice.service;

import com.axis.billingmicroservice.client.PatientServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.axis.billingmicroservice.dto.PatientDto;
import com.axis.billingmicroservice.entity.Billing;
import com.axis.billingmicroservice.exception.InvalidPaymentMethodException;
import com.axis.billingmicroservice.exception.NoAppointmentException;
import com.axis.billingmicroservice.repository.BillingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillingService {

	@Autowired
	private PatientServiceClient patientServiceClient;

	@Autowired
	private BillingRepository billingRepository;

	private static final List<String> VALID_PAYMENT_METHODS = List.of("Online", "Cash", "Prepaid");

	public List<Billing> getAllBillings() {
		return billingRepository.findAll();
	}

	public Billing createBilling(Long clinicId, Long patientId, Double amount, String paymentMethod) {
		PatientDto patient = patientServiceClient.getPatientById(patientId);

		if (!VALID_PAYMENT_METHODS.contains(paymentMethod)) {
			throw new InvalidPaymentMethodException(paymentMethod);
		}

		if (patientId != null) {
			Billing billing = new Billing();
			billing.setClinicId(clinicId);
			billing.setPatientId(patientId);
			billing.setPatientFirstName(patient.getFirstName());
			billing.setPatientLastName(patient.getLastName());
			billing.setPatientEmail(patient.getEmail());
			billing.setAmount(amount);
			billing.setPaymentMethod(paymentMethod);
			billing.setPaymentDate(LocalDateTime.now());

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

	public double getTotalEarnings() {
		return billingRepository.findAll()
				.stream()
				.map(Billing::getAmount)
				.filter(amount -> amount != null)
				.mapToDouble(Double::doubleValue)
				.sum();
	}

	public Map<String, Long> getPaymentMethodsSummary() {
		return billingRepository.findAll()
				.stream()
				.collect(Collectors.groupingBy(Billing::getPaymentMethod, Collectors.counting()));
	}
}
