package com.axis.billingmicroservice.exception;

public class InvalidPaymentMethodException extends RuntimeException {
    public InvalidPaymentMethodException(String paymentMethod) {
        super("Invalid payment method: " + paymentMethod + ". Valid methods are: Online, Cash, Prepaid.");
    }
}