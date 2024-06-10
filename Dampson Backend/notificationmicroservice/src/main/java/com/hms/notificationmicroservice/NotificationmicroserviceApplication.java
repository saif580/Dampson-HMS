package com.hms.notificationmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NotificationmicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationmicroserviceApplication.class, args);
    }

}
