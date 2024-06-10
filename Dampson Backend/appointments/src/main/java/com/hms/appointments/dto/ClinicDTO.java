package com.hms.appointments.dto;

public class ClinicDTO {
    private Long clinicId;
    private String doctorName;
    private String clinicName;
    private String clinicTime;
    private int maxPatientsPerHour;
    private String address;
    private String contactNumber;
    private String email;
    private String clinicSpeciality;
    private String clinicFacilities;
    private String operatingDays;
    private int appointmentDuration;
    private double doctorFees;

    public ClinicDTO(Long clinicId, String doctorName, String clinicTime, String clinicName, int maxPatientsPerHour, String address, String email, String contactNumber, String clinicFacilities, String operatingDays, double doctorFees, int appointmentDuration, String clinicSpeciality) {
        this.clinicId = clinicId;
        this.doctorName = doctorName;
        this.clinicTime = clinicTime;
        this.clinicName = clinicName;
        this.maxPatientsPerHour = maxPatientsPerHour;
        this.address = address;
        this.email = email;
        this.contactNumber = contactNumber;
        this.clinicFacilities = clinicFacilities;
        this.operatingDays = operatingDays;
        this.doctorFees = doctorFees;
        this.appointmentDuration = appointmentDuration;
        this.clinicSpeciality = clinicSpeciality;
    }

    // Getters and Setters

    public Long getClinicId() {
        return clinicId;
    }

    public void setClinicId(Long clinicId) {
        this.clinicId = clinicId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getClinicTime() {
        return clinicTime;
    }

    public void setClinicTime(String clinicTime) {
        this.clinicTime = clinicTime;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public int getMaxPatientsPerHour() {
        return maxPatientsPerHour;
    }

    public void setMaxPatientsPerHour(int maxPatientsPerHour) {
        this.maxPatientsPerHour = maxPatientsPerHour;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClinicSpeciality() {
        return clinicSpeciality;
    }

    public void setClinicSpeciality(String clinicSpeciality) {
        this.clinicSpeciality = clinicSpeciality;
    }

    public String getOperatingDays() {
        return operatingDays;
    }

    public void setOperatingDays(String operatingDays) {
        this.operatingDays = operatingDays;
    }

    public int getAppointmentDuration() {
        return appointmentDuration;
    }

    public void setAppointmentDuration(int appointmentDuration) {
        this.appointmentDuration = appointmentDuration;
    }

    public String getClinicFacilities() {
        return clinicFacilities;
    }

    public void setClinicFacilities(String clinicFacilities) {
        this.clinicFacilities = clinicFacilities;
    }

    public double getDoctorFees() {
        return doctorFees;
    }

    public void setDoctorFees(double doctorFees) {
        this.doctorFees = doctorFees;
    }
}