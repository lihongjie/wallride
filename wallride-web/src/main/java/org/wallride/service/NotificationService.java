package org.wallride.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.wallride.domain.Notification;
import org.wallride.domain.User;

import java.util.List;

public interface NotificationService {

    List<Notification> findTop10NotificationByUser(User user);

    long countByUser(User user);

    long countByUser(Long userId);

    long countUnRead();

    Page<Notification> getNotifications(Pageable pageable);

    List<Notification> markAsRead(List<Notification> notifications);
}
