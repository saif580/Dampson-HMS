package com.hms.notificationmicroservice.service;

import com.hms.notificationmicroservice.entity.Notification;
import com.hms.notificationmicroservice.entity.NotificationRequest;
import com.hms.notificationmicroservice.repository.NotificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    public Notification sendNotification(NotificationRequest notificationRequest) {
        Notification notification = new Notification();
        notification.setAppointmentId(notificationRequest.getAppointmentId());
        notification.setPatientId(notificationRequest.getPatientId());
        notification.setEmail(notificationRequest.getEmail());
        notification.setSubject(notificationRequest.getSubject());
        notification.setMessage(notificationRequest.getMessage());
        notification.setStatus(notificationRequest.getStatus());

        try {
            logger.info("Creating MIME message for notification");
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(notification.getEmail());
            helper.setSubject(notification.getSubject());
            helper.setText(notification.getMessage(), true);

            logger.info("Sending email to: {}", notification.getEmail());
            javaMailSender.send(mimeMessage);

            notification.setStatus("Sent");
            logger.info("Email sent successfully to: {}", notification.getEmail());
        } catch (MessagingException e) {
            logger.error("Failed to send email to: {}", notification.getEmail(), e);
            notification.setStatus("Failed");
        }

        return notificationRepository.save(notification);
    }

    public void sendTestEmail(String to, String subject, String body) {
        try {
            logger.info("Creating MIME message for test email");
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            logger.info("Sending test email to: {}", to);
            javaMailSender.send(mimeMessage);
            logger.info("Test email sent successfully to: {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send test email to: {}", to, e);
        }
    }
}
