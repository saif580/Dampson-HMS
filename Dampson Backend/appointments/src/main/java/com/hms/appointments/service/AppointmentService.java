package com.hms.appointments.service;

import com.hms.appointments.client.ClinicClient;
import com.hms.appointments.dto.ClinicDTO;
import com.hms.appointments.dto.NotificationRequest;
import com.hms.appointments.dto.PaymentRequest;
import com.hms.appointments.entity.Appointment;
import com.hms.appointments.repository.AppointmentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class AppointmentService {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ClinicClient clinicClient;

    @Autowired
    private PaymentService paymentService;

    public Appointment bookAppointment(Appointment appointment) throws StripeException {
        logger.info("Received appointment: {}", appointment);

        // Fetch clinic details using Feign client
        Long defaultClinicId = 1L; // Assuming there's only one clinic with ID 1
        ClinicDTO clinic = clinicClient.getClinicById(defaultClinicId);
        if (clinic == null) {
            logger.error("Clinic not found");
            throw new IllegalArgumentException("Clinic not found");
        }

        if (appointment.getName() == null || appointment.getName().isEmpty()) {
            logger.error("Name cannot be empty");
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (appointment.getEmail() == null || appointment.getEmail().isEmpty()) {
            logger.error("Email cannot be empty");
            throw new IllegalArgumentException("Email cannot be empty");
        }

        // Validate multiple appointments with the same name and email on the same day
        List<Appointment> existingAppointments = appointmentRepository.findByEmail(appointment.getEmail());
        for (Appointment existingAppointment : existingAppointments) {
            if (existingAppointment.getAppointmentDate() != null
                    && existingAppointment.getAppointmentDate().equals(appointment.getAppointmentDate())
                    && existingAppointment.getName().equals(appointment.getName())) {
                logger.error("Cannot book multiple appointments with the same name and email on the same day");
                throw new IllegalArgumentException("Cannot book multiple appointments with the same name and email on the same day");
            }
        }

        // Additional validation using clinic information
        if (appointment.getAppointmentTime() == null) {
            logger.error("Appointment time cannot be empty");
            throw new IllegalArgumentException("Appointment time cannot be empty");
        }

        // Check if the appointment time is within clinic operating hours
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
        LocalTime clinicStartTime = LocalTime.parse(clinic.getClinicTime().split(" - ")[0], timeFormatter);
        LocalTime clinicEndTime = LocalTime.parse(clinic.getClinicTime().split(" - ")[1], timeFormatter);

        LocalTime appointmentTime = appointment.getAppointmentTime();
        if (appointmentTime.isBefore(clinicStartTime) || appointmentTime.isAfter(clinicEndTime)) {
            logger.error("Appointment time is outside clinic operating hours");
            throw new IllegalArgumentException("Appointment time is outside clinic operating hours. Please choose a time between " + clinic.getClinicTime());
        }

        // Check if the appointment date is within clinic operating days
        LocalDate appointmentDate = LocalDate.parse(appointment.getAppointmentDate());
        String appointmentDayOfWeek = appointmentDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        List<String> operatingDays = convertOperatingDays(clinic.getOperatingDays());
        if (!operatingDays.contains(appointmentDayOfWeek)) {
            logger.error("Appointment date is outside clinic operating days");
            throw new IllegalArgumentException("Appointment date is outside clinic operating days. Please choose a date within the operating days: " + clinic.getOperatingDays());
        }

        // Check if the max patients per hour is reached
        int appointmentCount = countAppointmentsAtTime(appointment.getAppointmentDate(), appointmentTime);
        if (appointmentCount >= clinic.getMaxPatientsPerHour()) {
            logger.error("Maximum number of patients per hour reached");
            throw new IllegalArgumentException("Maximum number of patients per hour reached for the selected time slot. Please choose a different time.");
        }

        logger.info("Booking appointment for: {}", appointment.getEmail());
        appointment.setStatus("NOT PAID");
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Log the saved appointment to see if it has an ID
        logger.debug("Saved appointment: {}", savedAppointment);
        // Create a payment session
        PaymentRequest paymentRequest = new PaymentRequest((int) (clinic.getDoctorFees() * 100), "Appointment Fee");
        paymentRequest.setAppointmentId(savedAppointment.getId());
        paymentRequest.setDescription("Appointment Fee");
        Session paymentSession = paymentService.createCheckoutSession(paymentRequest);

        logger.debug("Created PaymentRequest: {}", paymentRequest);

        // Construct the message content with all required details
        String bookingMessage = String.format(
                "Your appointment is booked for %s at %s. Here are the details of the clinic:<br/><br/>" +
                        "Clinic Name: %s<br/>" +
                        "Doctor Name: %s<br/>" +
                        "Contact Number: %s<br/>" +
                        "Email: %s<br/>" +
                        "Clinic Speciality: %s<br/>" +
                        "Clinic Time: %s<br/>" +
                        "Operating Days: %s<br/>" +
                        "Doctor Fees: %.2f<br/>" +
                        "Address: %s",
                savedAppointment.getAppointmentDate(), savedAppointment.getAppointmentTime(),
                clinic.getClinicName(), clinic.getDoctorName(), clinic.getContactNumber(), clinic.getEmail(),
                clinic.getClinicSpeciality(), clinic.getClinicTime(), clinic.getOperatingDays(), clinic.getDoctorFees(),
                clinic.getAddress()
        );

        // Send notification
        String notificationServiceUrl = "http://notificationmicroservice/notifications/send";
        NotificationRequest notificationRequest = new NotificationRequest(
                savedAppointment.getId(),
                savedAppointment.getEmail(),
                "Appointment booked successfully",
                bookingMessage,
                "Pending",
                "BOOKING_CONFIRMATION",
                clinic.getClinicName(),
                clinic.getDoctorName(),
                clinic.getContactNumber(),
                clinic.getEmail(),
                clinic.getClinicSpeciality(),
                clinic.getClinicTime(),
                clinic.getOperatingDays(),
                clinic.getDoctorFees()
        );

        try {
            restTemplate.postForObject(notificationServiceUrl, notificationRequest, String.class);
            logger.info("Notification for booking sent to: {}", savedAppointment.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send booking notification", e);
        }

        // Construct the fee payment message with all required details
        String feePaymentMessage = String.format(
                "Please pay the fee for your appointment by clicking the button below: <br/><br/>" +
                        "<a href='%s' " +
                        "style='display: inline-block; padding: 10px 20px; font-size: 16px; color: white; background-color: #007bff; text-align: center; text-decoration: none; border-radius: 5px;'>Pay Now</a><br/><br/>" +
                        "Here are the details of the clinic:<br/><br/>" +
                        "Clinic Name: %s<br/>" +
                        "Doctor Name: %s<br/>" +
                        "Contact Number: %s<br/>" +
                        "Email: %s<br/>" +
                        "Clinic Speciality: %s<br/>" +
                        "Clinic Time: %s<br/>" +
                        "Operating Days: %s<br/>" +
                        "Doctor Fees: %.2f<br/>" +
                        "Address: %s",
                paymentService.getCheckoutUrl(paymentSession),
                clinic.getClinicName(), clinic.getDoctorName(), clinic.getContactNumber(), clinic.getEmail(),
                clinic.getClinicSpeciality(), clinic.getClinicTime(), clinic.getOperatingDays(), clinic.getDoctorFees(),
                clinic.getAddress()
        );

        NotificationRequest feeNotificationRequest = new NotificationRequest(
                savedAppointment.getId(),
                savedAppointment.getEmail(),
                "Fee Payment",
                feePaymentMessage,
                "Pending",
                "FEE_PAYMENT",
                clinic.getClinicName(),
                clinic.getDoctorName(),
                clinic.getContactNumber(),
                clinic.getEmail(),
                clinic.getClinicSpeciality(),
                clinic.getClinicTime(),
                clinic.getOperatingDays(),
                clinic.getDoctorFees()
        );

        try {
            restTemplate.postForObject(notificationServiceUrl, feeNotificationRequest, String.class);
            logger.info("Fee payment notification sent to: {}", savedAppointment.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send fee payment notification", e);
        }

        return savedAppointment;
    }

    private int countAppointmentsAtTime(String date, LocalTime time) {
        Query query = new Query();
        query.addCriteria(Criteria.where("appointmentDate").is(date)
                .and("appointmentTime").is(time.toString()));
        return (int) mongoTemplate.count(query, Appointment.class);
    }

    private List<String> convertOperatingDays(String operatingDays) {
        List<String> days = new ArrayList<>();
        if (operatingDays.equalsIgnoreCase("Monday to Friday")) {
            days = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
        } else {
            days = Arrays.asList(operatingDays.split(",\\s*"));
        }
        return days;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
}
