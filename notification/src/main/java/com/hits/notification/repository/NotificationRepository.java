package com.hits.notification.repository;

import com.hits.notification.model.Notification;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface NotificationRepository extends CrudRepository<Notification, UUID> {
}
