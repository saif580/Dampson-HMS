package com.hms.notificationmicroservice.controller;

import com.hms.notificationmicroservice.entity.NotificationRequest;
import com.hms.notificationmicroservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public void sendNotification(@RequestBody NotificationRequest notificationRequest) {
        notificationService.sendNotification(notificationRequest);
    }
    @GetMapping("/test-email")
    public String sendTestEmail() {
        String recipient = "saifulhasan580@gmail.com";
        String subject = "Test Subject";
        String message = "Test Message";

        logger.info("Initiating test email to {}", recipient);

        try {
            notificationService.sendTestEmail(recipient, subject, message);
            logger.info("Test email sent successfully to {}", recipient);
            return "Email sent";
        } catch (Exception e) {
            logger.error("Failed to send test email to {}", recipient, e);
            return "Failed to send email";
        }
    }
}