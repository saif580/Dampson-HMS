package com.hms.notificationmicroservice.controller;

import com.hms.notificationmicroservice.entity.NotificationRequest;
import com.hms.notificationmicroservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public void sendNotification(@RequestBody NotificationRequest notificationRequest) {
        notificationService.sendNotification(notificationRequest);
    }
    @GetMapping("/test-email")
    public String sendTestEmail() {
        notificationService.sendTestEmail("saifulhasan580@gmail.com", "Test Subject", "Test Message");
        return "Email sent";
    }
}