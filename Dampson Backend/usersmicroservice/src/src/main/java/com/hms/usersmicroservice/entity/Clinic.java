package com.hms.usersmicroservice.entity;

import com.sun.istack.NotNull;
import jakarta.persistence.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
public class Clinic {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private double doctorFees;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    
    
    public Clinic(Long clinicId, User doctor, @NotBlank String doctorName, @NotBlank String clinicName,
			@NotBlank String clinicTime, int maxPatientsPerHour, String address, String contactNumber, String email,
			String clinicSpeciality, String clinicFacilities, String operatingDays, int appointmentDuration,
			double doctorFees, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.clinicId = clinicId;
		this.doctor = doctor;
		this.doctorName = doctorName;
		this.clinicName = clinicName;
		this.clinicTime = clinicTime;
		this.maxPatientsPerHour = maxPatientsPerHour;
		this.address = address;
		this.contactNumber = contactNumber;
		this.email = email;
		this.clinicSpeciality = clinicSpeciality;
		this.clinicFacilities = clinicFacilities;
		this.operatingDays = operatingDays;
		this.appointmentDuration = appointmentDuration;
		this.doctorFees = doctorFees;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
    
    
    public Clinic() {
		super();
	}

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

    public double getDoctorFees() {
        return doctorFees;
    }

    public void setDoctorFees(double doctorFees) {
        this.doctorFees = doctorFees;
    }
}
