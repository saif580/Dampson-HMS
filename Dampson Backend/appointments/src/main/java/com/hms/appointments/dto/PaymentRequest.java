package com.hms.appointments.dto;

public class PaymentRequest {
    private String appointmentId;
    private long amount;
    private String description;

    public PaymentRequest(int amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    // Getters and Setters

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
