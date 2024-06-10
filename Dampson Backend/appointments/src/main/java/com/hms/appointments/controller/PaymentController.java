package com.hms.appointments.controller;

import com.hms.appointments.entity.Appointment;
import com.hms.appointments.entity.Transaction;
import com.hms.appointments.repository.AppointmentRepository;
import com.hms.appointments.repository.TransactionRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/payment-success")
    public void paymentSuccess(@RequestParam("session_id") String sessionId, HttpServletResponse response) {
        logger.debug("Entering paymentSuccess with sessionId: {}", sessionId);

        try {
            // Load the HTML template as a string
            Resource resource = new ClassPathResource("templates/payment-success.html");
            if (!resource.exists()) {
                logger.error("Template file payment-success.html not found in templates directory.");
                sendErrorResponse(response, "Template file not found. Please contact support.");
                return;
            }

            String htmlContent = new String(Files.readAllBytes(Paths.get(resource.getURI())));
            logger.debug("Loaded HTML template content: {}", htmlContent);

            // Retrieve session details from Stripe
            Session session = Session.retrieve(sessionId);
            logger.debug("Retrieved Stripe session: {}", session);

            String amount = String.format("%.2f", session.getAmountTotal() / 100.0); // amount in dollars
            logger.debug("Calculated amount: {}", amount);

            // Validate metadata for appointmentId
            String appointmentId = session.getMetadata().get("appointmentId");
            if (appointmentId == null) {
                throw new IllegalArgumentException("Appointment ID is null in metadata");
            }
            logger.debug("Appointment ID: {}", appointmentId);

            // Update appointment status to PAID
            Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
            if (optionalAppointment.isPresent()) {
                Appointment appointment = optionalAppointment.get();
                appointment.setStatus("PAID");
                appointmentRepository.save(appointment);
                logger.debug("Updated appointment status to PAID for appointment ID: {}", appointmentId);

                // Add a new transaction entry
                Transaction transaction = new Transaction();
                transaction.setAppointmentId(appointmentId);
                transaction.setPaymentId(session.getPaymentIntent());
                transaction.setAmount(BigDecimal.valueOf(session.getAmountTotal() / 100.0));
                transaction.setStatus("PAID");
                transaction.setCreatedAt(LocalDateTime.now());
                transactionRepository.save(transaction);
                logger.debug("Added transaction for appointment ID: {}", appointmentId);
            } else {
                throw new IllegalArgumentException("Appointment not found for ID: " + appointmentId);
            }

            // Replace placeholders with actual values
            htmlContent = htmlContent.replace("<!-- AMOUNT_PLACEHOLDER -->", amount);
            logger.debug("Modified HTML content: {}", htmlContent);

            // Write the modified HTML content to the response
            response.setContentType("text/html");
            response.getWriter().write(htmlContent);

            logger.debug("Session retrieved successfully. Amount: {}", amount);
        } catch (StripeException e) {
            logger.error("Error retrieving Stripe session", e);
            sendErrorResponse(response, "Error retrieving session details. Please contact support.");
        } catch (IOException e) {
            logger.error("Error loading HTML template", e);
            sendErrorResponse(response, "Error loading HTML template. Please contact support.");
        } catch (Exception e) {
            logger.error("Unexpected error occurred", e);
            sendErrorResponse(response, "An unexpected error occurred. Please contact support.");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String errorMessage) {
        try {
            // Load the HTML template as a string
            Resource resource = new ClassPathResource("templates/payment-success.html");
            String htmlContent = new String(Files.readAllBytes(Paths.get(resource.getURI())));
            logger.debug("Loaded error HTML template content: {}", htmlContent);

            // Replace placeholders with the error message
            htmlContent = htmlContent.replace("<!-- AMOUNT_PLACEHOLDER -->", "");
            htmlContent = htmlContent.replace("<!-- ERROR_PLACEHOLDER -->", errorMessage);
            logger.debug("Modified error HTML content: {}", htmlContent);

            // Write the modified HTML content to the response
            response.setContentType("text/html");
            response.getWriter().write(htmlContent);
        } catch (IOException e) {
            logger.error("Error sending error response", e);
        }
    }
}
