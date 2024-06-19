package com.hms.usersmicroservice.dto;

public class Appointment {
    private String id;
    private String name;
    private String email;
    private String appointmentDate;
    private String appointmentTime;
    private String status;

    // Getters and Setters
    
    
    public Appointment(String id, String name, String email, String appointmentDate, String appointmentTime,
			String status) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.appointmentDate = appointmentDate;
		this.appointmentTime = appointmentTime;
		this.status = status;
	}
    
    public Appointment() {
		super();
	}

	public String getId() {
        return id;
    }


	public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}