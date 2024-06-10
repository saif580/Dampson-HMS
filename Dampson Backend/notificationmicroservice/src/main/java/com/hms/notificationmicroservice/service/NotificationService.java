package com.hms.notificationmicroservice.service;

import com.hms.notificationmicroservice.entity.Notification;
import com.hms.notificationmicroservice.entity.NotificationRequest;
import com.hms.notificationmicroservice.repository.NotificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

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
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(notification.getEmail());
            helper.setSubject(notification.getSubject());
            helper.setText(notification.getMessage(), true);

            javaMailSender.send(mimeMessage);

            notification.setStatus("Sent");
        } catch (MessagingException e) {
            notification.setStatus("Failed");
        }

        return notificationRepository.save(notification);
    }

    public void sendTestEmail(String to, String subject, String body) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}