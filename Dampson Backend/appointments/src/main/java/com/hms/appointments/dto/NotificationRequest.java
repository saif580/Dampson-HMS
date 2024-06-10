package com.hms.appointments.dto;

public class NotificationRequest {
    private String appointmentId;
    private String email;
    private String subject;
    private String message;
    private String status;
    private String notificationType;
    private String clinicName;
    private String doctorName;
    private String contactNumber;
    private String clinicEmail;
    private String clinicSpeciality;
    private String clinicTime;
    private String operatingDays;
    private double doctorFees;

    // Constructor with all fields
    public NotificationRequest(String appointmentId, String email, String subject, String message, String status, String notificationType,
                               String clinicName, String doctorName, String contactNumber, String clinicEmail,
                               String clinicSpeciality, String clinicTime, String operatingDays, double doctorFees) {
        this.appointmentId = appointmentId;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.status = status;
        this.notificationType = notificationType;
        this.clinicName = clinicName;
        this.doctorName = doctorName;
        this.contactNumber = contactNumber;
        this.clinicEmail = clinicEmail;
        this.clinicSpeciality = clinicSpeciality;
        this.clinicTime = clinicTime;
        this.operatingDays = operatingDays;
        this.doctorFees = doctorFees;
    }

    // Getters and setters

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getClinicEmail() {
        return clinicEmail;
    }

    public void setClinicEmail(String clinicEmail) {
        this.clinicEmail = clinicEmail;
    }

    public String getClinicSpeciality() {
        return clinicSpeciality;
    }

    public void setClinicSpeciality(String clinicSpeciality) {
        this.clinicSpeciality = clinicSpeciality;
    }

    public String getClinicTime() {
        return clinicTime;
    }

    public void setClinicTime(String clinicTime) {
        this.clinicTime = clinicTime;
    }

    public String getOperatingDays() {
        return operatingDays;
    }

    public void setOperatingDays(String operatingDays) {
        this.operatingDays = operatingDays;
    }

    public double getDoctorFees() {
        return doctorFees;
    }

    public void setDoctorFees(double doctorFees) {
        this.doctorFees = doctorFees;
    }
}
