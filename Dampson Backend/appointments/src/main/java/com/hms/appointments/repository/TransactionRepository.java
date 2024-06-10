package com.hms.appointments.repository;

import com.hms.appointments.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByPaymentId(String paymentId);
}
