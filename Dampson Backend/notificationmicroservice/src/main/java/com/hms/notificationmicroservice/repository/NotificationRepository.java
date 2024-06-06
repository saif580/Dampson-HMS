package com.hms.notificationmicroservice.repository;

import com.hms.notificationmicroservice.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}
