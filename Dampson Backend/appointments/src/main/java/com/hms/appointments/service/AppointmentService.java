package com.hms.appointments.service;

import com.hms.appointments.dto.NotificationRequest;
import com.hms.appointments.entity.Appointment;
import com.hms.appointments.repository.AppointmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AppointmentService {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Appointment bookAppointment(Appointment appointment) {
        logger.info("Received appointment: {}", appointment);

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

        logger.info("Booking appointment for: {}", appointment.getEmail());
        appointment.setStatus("NOT PAID");
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Send notification
        String notificationServiceUrl = "http://notificationmicroservice/notifications/send";
        NotificationRequest notificationRequest = new NotificationRequest(
                savedAppointment.getId(),
                savedAppointment.getEmail(),
                "Appointment booked successfully",
                "Your appointment is booked for " + savedAppointment.getAppointmentDate() + " at " + savedAppointment.getAppointmentTime(),
                "Pending",
                "BOOKING_CONFIRMATION"
        );

        try {
            restTemplate.postForObject(notificationServiceUrl, notificationRequest, String.class);
            logger.info("Notification for booking sent to: {}", savedAppointment.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send booking notification", e);
        }

        // Send second email for fee payment
        String feePaymentMessage = "Please pay the fee for your appointment by clicking the button below: <br/><br/>" +
                "<a href='http://yourpaymentlink.com/pay?appointmentId=" + savedAppointment.getId() + "' " +
                "style='display: inline-block; padding: 10px 20px; font-size: 16px; color: white; background-color: #007bff; text-align: center; text-decoration: none; border-radius: 5px;'>Pay Now</a>";

        NotificationRequest feeNotificationRequest = new NotificationRequest(
                savedAppointment.getId(),
                savedAppointment.getEmail(),
                "Fee Payment",
                feePaymentMessage,
                "Pending",
                "FEE_PAYMENT"
        );

        try {
            restTemplate.postForObject(notificationServiceUrl, feeNotificationRequest, String.class);
            logger.info("Fee payment notification sent to: {}", savedAppointment.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send fee payment notification", e);
        }

        return savedAppointment;
    }
}
