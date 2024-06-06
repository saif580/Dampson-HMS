package com.hms.usersmicroservice.entity;

import com.sun.istack.NotNull;
import jakarta.persistence.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clinicId;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @NotBlank
    private String doctorName;

    @NotBlank
    private String clinicName;

    @NotBlank
    private String clinicTime;

    @NotNull
    private int maxPatientsPerHour;

    private String address;
    private String contactNumber;
    private String email;
    private String clinicSpeciality;
    private String clinicFacilities;
    private String operatingDays;
    private int appointmentDuration;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters

    public Long getClinicId() {
        return clinicId;
    }

    public void setClinicId(Long clinicId) {
        this.clinicId = clinicId;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getAppointmentDuration() {
        return appointmentDuration;
    }

    public void setAppointmentDuration(int appointmentDuration) {
        this.appointmentDuration = appointmentDuration;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getOperatingDays() {
        return operatingDays;
    }

    public void setOperatingDays(String operatingDays) {
        this.operatingDays = operatingDays;
    }

    public String getClinicFacilities() {
        return clinicFacilities;
    }

    public void setClinicFacilities(String clinicFacilities) {
        this.clinicFacilities = clinicFacilities;
    }

    public String getClinicSpeciality() {
        return clinicSpeciality;
    }

    public void setClinicSpeciality(String clinicSpeciality) {
        this.clinicSpeciality = clinicSpeciality;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getMaxPatientsPerHour() {
        return maxPatientsPerHour;
    }

    public void setMaxPatientsPerHour(int maxPatientsPerHour) {
        this.maxPatientsPerHour = maxPatientsPerHour;
    }

    public @NotBlank String getClinicTime() {
        return clinicTime;
    }

    public void setClinicTime(@NotBlank String clinicTime) {
        this.clinicTime = clinicTime;
    }

    public @NotBlank String getClinicName() {
        return clinicName;
    }

    public void setClinicName(@NotBlank String clinicName) {
        this.clinicName = clinicName;
    }

    public @NotBlank String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(@NotBlank String doctorName) {
        this.doctorName = doctorName;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }
}
