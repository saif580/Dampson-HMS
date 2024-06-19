package com.axis.billingmicroservice.exception;

public class NoAppointmentException extends RuntimeException {
    public NoAppointmentException(Long patientId) {
        super("Patient with ID " + patientId + " does not have an appointment.");
    }
}