package com.hms.medicalrecordmicroservice.dto;

public class Clinic {
    private Long clinicId;
    private String clinicName;
    private String clinicTime;
    private String address;
    private String contactNumber;
    private String email;
    private String clinicSpeciality;
    private String clinicFacilities;
    private String operatingDays;

    public Long getClinicId() {
        return clinicId;
    }

    public void setClinicId(Long clinicId) {
        this.clinicId = clinicId;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getClinicTime() {
        return clinicTime;
    }

    public void setClinicTime(String clinicTime) {
        this.clinicTime = clinicTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
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

    public String getClinicFacilities() {
        return clinicFacilities;
    }

    public void setClinicFacilities(String clinicFacilities) {
        this.clinicFacilities = clinicFacilities;
    }

    public String getOperatingDays() {
        return operatingDays;
    }

    public void setOperatingDays(String operatingDays) {
        this.operatingDays = operatingDays;
    }
}
