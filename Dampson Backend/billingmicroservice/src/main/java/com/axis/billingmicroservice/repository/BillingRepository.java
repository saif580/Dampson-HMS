package com.axis.billingmicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.axis.billingmicroservice.entity.Billing;

import java.util.List;

public interface BillingRepository extends JpaRepository<Billing, Long> {
    List<Billing> findByPatientId(Long patientId);
}